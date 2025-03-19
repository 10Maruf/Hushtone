package HushCrypto;
import HushCrypto.AES;
import HushCrypto.AES.AES_CTX;
import HushCrypto.CBCMode;
import HushCrypto.SteganographyUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class DecodeTab extends VBox {
    private TextField decodeInputField = new TextField();
    private TextField decodeKeyField = new TextField();
    private TextArea decodeResultArea = new TextArea();
    private Label statusLabel = new Label("Ready");
    private FileChooser fileChooser = new FileChooser();

    public DecodeTab() {
        setSpacing(10);
        setPadding(new Insets(10));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files", "*.wav"));

        decodeInputField.setPromptText("Drag & drop WAV file here...");
        decodeInputField.setEditable(false);
        Button decodeInputBrowseBtn = new Button("Browse");
        HBox inputHBox = new HBox(10, decodeInputField, decodeInputBrowseBtn);
        inputHBox.setAlignment(Pos.CENTER_LEFT);

        decodeKeyField.setPromptText("Enter decryption key");

        Button decodeButton = new Button("Decode Message");
        decodeResultArea.setPromptText("Decoded message will appear here...");
        decodeResultArea.setEditable(false);
        decodeResultArea.setPrefRowCount(4);

        getChildren().addAll(
                new Label("Input Audio File:"), inputHBox,
                new Label("Decryption Key:"), decodeKeyField,
                decodeButton,
                new Label("Decoded Message:"), decodeResultArea,
                statusLabel
        );

        enableDragAndDrop(decodeInputField);

        decodeInputBrowseBtn.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                decodeInputField.setText(file.getAbsolutePath());
            }
        });

        decodeButton.setOnAction(e -> decodeAction());
    }

    private void decodeAction() {
        String inputPath = decodeInputField.getText();
        String key = decodeKeyField.getText();
        if (inputPath.isEmpty() || key.isEmpty()) {
            statusLabel.setText("Please select an audio file and enter the decryption key.");
            return;
        }
        try {
            File inputFile = new File(inputPath);
            byte[] audioBytes = SteganographyUtils.readAudioFile(inputFile);
            byte[] encryptedMessage = SteganographyUtils.decodeMessageBytes(audioBytes);
            String decryptedMessage = SteganographyUtils.decrypt(encryptedMessage, key);
            decodeResultArea.setText(decryptedMessage);
            statusLabel.setText("Decoding and decryption successful.");
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // drag-and-drop 
    private void enableDragAndDrop(TextField textField) {
        textField.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        textField.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                textField.setText(file.getAbsolutePath());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }
}
