package HushCrypto;

import HushCrypto.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
       
        Image titleLogo = new Image(getClass().getResourceAsStream("/logo_icon.png"));
        Image mainLogo = new Image(getClass().getResourceAsStream("/logo.png"));

      
        ImageView logoView = new ImageView(mainLogo);
        logoView.setFitWidth(2128 / 8);
        logoView.setFitHeight(334 / 8);
        VBox logoBox = new VBox(logoView);
        logoBox.setPadding(new Insets(10));
        logoBox.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-background-color: white;");
        logoBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(logoBox);

        TabPane tabPane = new TabPane();
        Tab encodeTab = new Tab("Encode", new EncodeTab());
        Tab decodeTab = new Tab("Decode", new DecodeTab());
        encodeTab.setClosable(false);
        decodeTab.setClosable(false);
        tabPane.getTabs().addAll(encodeTab, decodeTab);
        root.setCenter(tabPane);

        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setStyle("-fx-background-color: #DDDDDD;");
        Button aboutButton = new Button("About");
        Button exitButton = new Button("Exit");
        aboutButton.setOnAction(e -> showAboutDialog(primaryStage, titleLogo));
        exitButton.setOnAction(e -> exitConfirmation(primaryStage));
        bottomBar.getChildren().addAll(aboutButton, exitButton);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hushtone");
        primaryStage.getIcons().add(titleLogo);

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            exitConfirmation(primaryStage);
        });

        showSplashScreen(primaryStage, scene, mainLogo);
    }

  
    private void showSplashScreen(Stage primaryStage, Scene mainScene, Image splashLogo) {
        Stage splashStage = new Stage();
        
        splashStage.setTitle("Hushtone");

        VBox splashLayout = new VBox(10);
        splashLayout.setPadding(new Insets(20));
        splashLayout.setAlignment(Pos.CENTER);
        splashLayout.setStyle("-fx-background-color: white; -fx-border-color: black;");

        ImageView splashLogoView = new ImageView(splashLogo);
        splashLogoView.setFitWidth(2128 / 8);
        splashLogoView.setFitHeight(334 / 8);

        
        splashLayout.getChildren().addAll(splashLogoView);
        
        Scene splashScene = new Scene(splashLayout, 400, 300);
        splashStage.setScene(splashScene);
        splashStage.initModality(Modality.APPLICATION_MODAL);
        splashStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo_icon.png")));
        splashStage.show();

        FadeTransition fade = new FadeTransition(Duration.seconds(2), splashLayout);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> {
            splashStage.close();
            primaryStage.show();
        });
        fade.play();
    }

    //About 
private void showAboutDialog(Stage owner, Image titleLogo) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.initOwner(owner);
    alert.setTitle("About Hushtone");
    alert.setHeaderText("Hushtone - Secure Audio Steganography");

    ImageView aboutLogo = new ImageView(titleLogo);
    aboutLogo.setFitWidth(70);
    aboutLogo.setFitHeight(70);

    VBox content = new VBox(10);
    content.setAlignment(Pos.CENTER);

    Label info = new Label(
            "Hushtone v1.0\n" +
            "Developed by Mofassel Alam Maruf\n" +
            "B.Sc. in Software Engineering, IIT, NSTU\n\n" +
            "This software provides secure audio steganography\n" +
            "with AES-128 encryption (PKCS#5 padding, CBC mode)."
    );
    info.setWrapText(true);
    info.setTextAlignment(TextAlignment.CENTER);

    content.getChildren().addAll(aboutLogo, info);
    
    alert.getDialogPane().setContent(content);
    alert.showAndWait();
}


    //exit
    private void exitConfirmation(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
