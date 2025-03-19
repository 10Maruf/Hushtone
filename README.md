# 🛡️ Hushtone - Secure Audio Steganography  

![Hushtone Logo](logo.jpg)  

Hushtone is a professional audio steganography tool that lets you securely hide and retrieve messages within 16‑bit PCM WAV audio files. It uses a raw implementation of AES‑128 encryption (with CBC mode and PKCS#5 padding) to ensure maximum confidentiality.

---

## 📌 Features  
✔️ Hide secret messages inside audio files  
✔️ Extract hidden messages with decryption  
✔️ AES-128 encryption with PKCS#5 padding (CBC mode)  
✔️ User-friendly graphical interface (JavaFX)   

---
## 🛠️ Requirements

- **WAV Files:** The input audio file must be a 16‑bit PCM WAV file.
- **Java 17+ Runtime:** Your system must have a Java Runtime Environment.  
  (Alternatively, use the self-contained jar provided.)

---

## 🖼️ Screenshots  

### 🔹 Encode Message  
![Encoding Screenshot](encode.png)  

### 🔹 Decode Message  
![Decoding Screenshot](decode.png)  

---

1. **Fetch the Hushtone Folder:**  
   Clone or download the Hushtone folder from GitHub:
   ```sh
   git clone https://github.com/10Maruf/Hushtone.git
   
2. **Download Java:**  
   Visit the [Official Java Website](https://www.oracle.com/java/technologies/downloads/) to download and install Java (version 17 or later).


3. **Set Up Environment Variables (if not already set):**  
   **On Windows:**  
   Open Command Prompt and set your `JAVA_HOME` to the JDK installation folder, then add the JDK `bin` directory to your `PATH`:
      ```sh
      set JAVA_HOME=C:\Program Files\Java\jdk-17
      set PATH=%JAVA_HOME%\bin;%PATH%
      ```

4. **Open a Terminal:**  
   Navigate to the root of the Hushtone folder (the folder containing your jar and the lib folder):
     ```sh
      cd path/to/Hushtone

5. **Run the Application:**  
   Execute the following command in the terminal:
     ```sh
     java --module-path "lib\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar HushCrypto.jar

**Note: Do not run the .exe file.**


## 🎯 Usage  

1️⃣ **Encoding**  
   - Select an audio file (`.wav` format recommended).  
   - Enter the secret message.  
   - Encrypt and save the modified audio file.  

2️⃣ **Decoding**  
   - Load the encoded audio file.  
   - Enter the decryption key (AES-128).  
   - Retrieve the hidden message.  

---

## ⚙️ Technologies Used  

- **Java 17+**  
- **JavaFX** (GUI framework)  
- **AES-128 Encryption** (PKCS#5, CBC mode)  

---
## 👤 Developer  

👨‍💻 **Mofassel Alam Maruf**  
📚 B.Sc. in Software Engineering, IIT, NSTU  
📧 [mofasselalammaruf@gmail.com](mailto:mofasselalammaruf@gmail.com)  
🌐 [GitHub Profile](https://github.com/10Maruf)  

---

## 📜 License  
📝 This project is licensed under the [MIT License](https://github.com/10Maruf/Hushtone/blob/main/LICENSE).  

---



🔒 **"Secure Your Messages, One Soundwave at a Time!"**  

