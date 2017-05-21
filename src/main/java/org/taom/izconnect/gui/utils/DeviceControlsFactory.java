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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;

import static org.taom.izconnect.gui.components.DevicesListItem.DeviceType;

public class DeviceControlsFactory {
    private static final int MAX_BYTES = 114500;
    private static String busName;
    private static final File SCRIPTS_FOLDER = new File(System.getProperty("user.home"), "izconnect/scripts");

    public static void setViewForDevice(Controller controller, ObservableList<Node> children, DevicesListItem device) {
        DeviceType deviceType = device.getDeviceType();
        VBox child;
        BiConsumer<String, byte[]> fileDataMethod;
        Consumer<String> runScriptMethod;
        switch (deviceType) {
            case BOARD:
                child = (VBox) setView(controller, children, "boardPane.fxml");
                final BoardInterface boardInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(BoardInterface.class);
                ToggleSwitch lightControl = (ToggleSwitch) child.lookup("#lightControl");
                lightControl.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
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
                        try {
                            boardInterface.setAutoMode(newValue);
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });

                fileDataMethod = (filename, data) -> {
                    try {
                        boardInterface.fileData(filename, data, true);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                runScriptMethod = (filename) -> {
                    try {
                        boardInterface.runScript(filename);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                setScriptsControls(controller, child, ".sh", "board", fileDataMethod, runScriptMethod);

                break;
            case MOBILE:
                child = (VBox) setView(controller, children, "mobilePane.fxml");
                final MobileInterface mobileInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(MobileInterface.class);

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

                fileDataMethod = (filename, data) -> {
                    try {
                        mobileInterface.fileData(filename, data, false);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                setFileTransferContext(controller, child, fileDataMethod);

                runScriptMethod = (filename) -> {
                    try {
                        mobileInterface.runScript(filename);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                setScriptsControls(controller, child, ".sh", "mobile", fileDataMethod, runScriptMethod);

                break;
            case PC:
                child = (VBox) setView(controller, children, "pcPane.fxml");
                final PCInterface pcInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(PCInterface.class);

                fileDataMethod = (filename, data) -> {
                    try {
                        pcInterface.fileData(filename, data, true);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                setFileTransferContext(controller, child, fileDataMethod);

                runScriptMethod = (filename) -> {
                    try {
                        pcInterface.runScript(filename);
                    } catch (BusException e) {
                        e.printStackTrace();
                    }
                };

                setScriptsControls(controller, child, ".bat", "pc", fileDataMethod, runScriptMethod);

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

    private static void setScriptsControls(Controller controller, VBox child, String scriptExtension, String scriptSubfolder,
                                           BiConsumer<String, byte[]> fileDataMethod, Consumer<String> runScriptMethod) {

        final File scriptFolder = new File(SCRIPTS_FOLDER, scriptSubfolder);
        scriptFolder.mkdirs();

        final FileChooser fileChooser = new FileChooser();
        VBox scriptHolder = (VBox) child.lookup("#customScripts");

        Button addButton = (Button) child.lookup("#scriptAddButton");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(child.getScene().getWindow());
                if (file != null) {
                    boolean exists = false;
                    if (!file.getName().endsWith(scriptExtension)) {
                        return;
                    }

                    final File localScriptLocation = new File(scriptFolder, file.getName());
                    if (localScriptLocation.exists())
                        exists = true;
                    try {
                        Files.copy(file.toPath(), localScriptLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!file.exists()) {
                                Platform.runLater(() -> controller.statusBar.setText("Script doesn't exist"));
                                return;
                            }

                            Platform.runLater(() -> {
                                addButton.setDisable(true);
                                controller.progressBar.setProgress(0);

                                controller.statusBar.setText("Adding script. . .");
                            });

                            sendFile(file, controller.progressBar, fileDataMethod);

                            Platform.runLater(() -> {
                                addButton.setDisable(false);
                                controller.statusBar.setText("Script added.");
                                controller.progressBar.setProgress(1);
                            });
                        }
                    });
                    thread.start();

                    if (!exists) {
                        Node scriptButtons = createScriptButtons(scriptHolder, localScriptLocation, runScriptMethod);
                        scriptHolder.getChildren().add(scriptButtons);
                    }
                }
            }
        });


        File[] scripts = scriptFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(scriptExtension);
            }
        });

        if (scripts != null) {
            for (File script : scripts) {
                Node scriptButtons = createScriptButtons(scriptHolder, script, runScriptMethod);
                scriptHolder.getChildren().add(scriptButtons);
            }
        }
    }

    private static Node createScriptButtons(VBox parent, File localScriptFile, Consumer<String> runScript) {
        HBox hbox = new HBox(10);

        Button runButton = new Button("Run " + localScriptFile.getName());
        runButton.setMinSize(100, 30);
        runButton.setFont(new Font(14));
        runButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                runScript.accept(localScriptFile.getName());
            }
        });
        hbox.getChildren().add(runButton);

        Button deleteButton = new Button("Remove " + localScriptFile.getName());
        deleteButton.setMinSize(100, 30);
        deleteButton.setFont(new Font(14));
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                parent.getChildren().remove(hbox);
                localScriptFile.delete();
            }
        });
        hbox.getChildren().add(deleteButton);

        return hbox;
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

                        sendFile(file, controller.progressBar, fileDataMethod);

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

    private static void sendFile(File file, ProgressBar progressBar, BiConsumer<String, byte[]> fileDataMethod) {
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
                Platform.runLater(() -> progressBar.setProgress(0.5 / maxProgress));
//                                try {
//                                    Thread.sleep(50);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
            }
            in.close();
            fileDataMethod.accept(file.getName(), new byte[0]);
            Platform.runLater(() -> progressBar.setProgress(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
