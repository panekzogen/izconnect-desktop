package org.taom.izconnect.gui.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.alljoyn.bus.BusException;
import org.controlsfx.control.ToggleSwitch;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.GFLogger;
import org.taom.izconnect.network.interfaces.BoardInterface;
import org.taom.izconnect.network.interfaces.MobileInterface;
import org.taom.izconnect.network.interfaces.PCInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;

import static org.taom.izconnect.gui.components.DevicesListItem.DeviceType;

public class DeviceControlsFactory {
    private static final int MAX_BYTES = 114500;
    private static String busName;

    public static void setViewForDevice(Controller controller, ObservableList<Node> children, DevicesListItem device) {
        DeviceType deviceType = device.getDeviceType();
        VBox child;
        switch (deviceType) {
            case BOARD:
                child = (VBox) setView(controller, children, "boardPane.fxml");
                ToggleSwitch lightControl = (ToggleSwitch) child.lookup("#lightControl");
                lightControl.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        BoardInterface boardInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(BoardInterface.class);
                        try {
                            boardInterface.setLight(newValue);
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ToggleSwitch autoMode = (ToggleSwitch) child.lookup("#autoMode");
                autoMode.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        BoardInterface boardInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(BoardInterface.class);
                        try {
                            boardInterface.setAutoMode(newValue);
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case MOBILE:
                child = (VBox) setView(controller, children, "mobilePane.fxml");
                MobileInterface mobileInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(MobileInterface.class);

                ToggleSwitch notificationsControl = (ToggleSwitch) child.lookup("#notifications");
                notificationsControl.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        try {
                            if (newValue) {
                                mobileInterface.subscribe(busName);
                            } else {
                                mobileInterface.unsubscribe(busName);
                            }
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });

                setFileTransferContext(controller, child, (filename, data) -> {
                    try {
                        mobileInterface.fileData(filename, data);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case PC:
                child = (VBox) setView(controller, children, "pcPane.fxml");
                PCInterface pcInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(PCInterface.class);

                setFileTransferContext(controller, child, (filename, data) -> {
                    try {
                        pcInterface.fileData(filename, data);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case UNKNOWN:
            default:
                break;
        }
    }

    private static Node setView(Controller controller, ObservableList<Node> children, String fxmlPath) {
        children.clear();
        Node child = null;
        try {
            child = FXMLLoader.load(controller.getClass().getClassLoader().getResource(fxmlPath));
            children.add(child);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, "DeviceControlsFactory", "Can't set controls view");
        }
        return child;
    }

    public static void setBusName(String busName) {
        DeviceControlsFactory.busName = busName;
    }

    private static void setFileTransferContext(Controller controller, VBox child, BiConsumer<String, byte[]> fileDataMethod) {
        final FileChooser fileChooser = new FileChooser();
        TextField filePath = (TextField) child.lookup("#filePath");
        Button chooseButton = (Button) child.lookup("#chooseButton");
        chooseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(child.getScene().getWindow());
                if (file != null) {
                    filePath.setText(file.getAbsolutePath());
                }
            }
        });
        Button sendButton = (Button) child.lookup("#sendButton");
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (filePath.getText() == null) {
                            Platform.runLater(() -> controller.statusBar.setText("Choose file at first"));
                            return;
                        }

                        File file = new File(filePath.getText());

                        if (!file.exists()) {
                            Platform.runLater(() -> controller.statusBar.setText("File doesn't exist"));
                            return;
                        }

                        Platform.runLater(() -> {
                            filePath.setDisable(true);
                            chooseButton.setDisable(true);
                            sendButton.setDisable(true);
                            controller.progressBar.setProgress(0);

                            controller.statusBar.setText("Sending the file . . .");
                        });

                        try {
                            FileInputStream in = new FileInputStream(file);
                            int length = (int) file.length();
                            byte[] tempBytes;
                            int numRead = 0;
                            int numChunks = length / MAX_BYTES + (length % MAX_BYTES == 0 ? 0 : 1);
                            int maxProgress = numChunks;
                            for (int i = 0; i < numChunks; i++) {
                                tempBytes = null;
                                int offset = 0;
                                numRead = 0;
                                if (MAX_BYTES > length) {
                                    tempBytes = new byte[length];
                                } else {
                                    tempBytes = new byte[MAX_BYTES];
                                }
                                while (offset < tempBytes.length && (numRead = in.read(tempBytes, 0, tempBytes.length - offset)) >= 0) {
                                    offset += numRead;
                                }
                                length -= MAX_BYTES;
                                fileDataMethod.accept(file.getName(), tempBytes);

                                int finalI = i;
                                Platform.runLater(() -> controller.progressBar.setProgress(0.5 / maxProgress));
//                                try {
//                                    Thread.sleep(50);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                            in.close();
                            fileDataMethod.accept(file.getName(), new byte[0]);
                            Platform.runLater(() -> controller.progressBar.setProgress(1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Platform.runLater(() -> {
                            controller.statusBar.setText("File transfering finished.");

                            filePath.setDisable(false);
                            chooseButton.setDisable(false);
                            sendButton.setDisable(false);
                        });

                    }
                });
                thread.start();
            }
        });
    }
}
