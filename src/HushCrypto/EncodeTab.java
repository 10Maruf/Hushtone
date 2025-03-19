package HushCrypto;
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

public class EncodeTab extends VBox {
    private TextField encodeInputField = new TextField();
    private TextField encodeOutputField = new TextField();
    private TextArea messageArea = new TextArea();
    private TextField keyField = new TextField();
    private Label statusLabel = new Label("Ready");
    private FileChooser fileChooser = new FileChooser();

    public EncodeTab() {
        setSpacing(10);
        setPadding(new Insets(10));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files", "*.wav"));

        encodeInputField.setPromptText("Drag & drop input WAV file here...");
        encodeInputField.setEditable(false);
        Button encodeInputBrowseBtn = new Button("Browse");
        HBox inputHBox = new HBox(10, encodeInputField, encodeInputBrowseBtn);
        inputHBox.setAlignment(Pos.CENTER_LEFT);

      
        encodeOutputField.setPromptText("Select output WAV file...");
        encodeOutputField.setEditable(false);
        Button encodeOutputBrowseBtn = new Button("Browse");
        HBox outputHBox = new HBox(10, encodeOutputField, encodeOutputBrowseBtn);

        messageArea.setPromptText("Enter secret message here...");
        messageArea.setPrefRowCount(4);
        keyField.setPromptText("Enter encryption key (min 1 char; padded/truncated to 16 bytes)");

     
        Button encodeButton = new Button("Encode Message");

        getChildren().addAll(
                new Label("Input Audio File:"), inputHBox,
                new Label("Output Audio File:"), outputHBox,
                new Label("Secret Message:"), messageArea,
                new Label("Encryption Key:"), keyField,
                encodeButton, statusLabel
        );

       
        enableDragAndDrop(encodeInputField);

 
        encodeInputBrowseBtn.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                encodeInputField.setText(file.getAbsolutePath());
            }
        });
        encodeOutputBrowseBtn.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(getScene().getWindow());
            if (file != null) {
                encodeOutputField.setText(file.getAbsolutePath());
            }
        });

      
        encodeButton.setOnAction(e -> encodeAction());
    }

    private void encodeAction() {
        String inputPath = encodeInputField.getText();
        String outputPath = encodeOutputField.getText();
        String secretMessage = messageArea.getText();
        String key = keyField.getText();
        if (inputPath.isEmpty() || outputPath.isEmpty() || secretMessage.isEmpty() || key.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }
        try {
            // Encrypt the message and then encode it into the audio file.
            byte[] encryptedMessage = SteganographyUtils.encrypt(secretMessage, key);
            File inputFile = new File(inputPath);
            byte[] audioBytes = SteganographyUtils.readAudioFile(inputFile);
            byte[] modifiedAudio = SteganographyUtils.encodeMessage(audioBytes, encryptedMessage);
            File outputFile = new File(outputPath);
            // Write the modified audio using the original file's format.
            SteganographyUtils.writeAudioFile(modifiedAudio, inputFile, outputFile);
            statusLabel.setText("Message encoded and encrypted successfully.");
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //  drag-and-drop 
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
