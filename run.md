javac --module-path "C:\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -d bin src\HushCrypto\*.java
java --module-path "C:\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin HushCrypto.App
jar cfe HushCrypto.jar HushCrypto.App -C bin .
java --module-path "C:\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar HushCrypto.jar
jlink --module-path "C:\Program Files\Java\jdk-22\jmods;C:\javafx-sdk-22.0.1\lib" --add-modules java.base,java.desktop,javafx.controls,javafx.fxml --output jre --strip-debug --compress 2 --no-header-files --no-man-pages