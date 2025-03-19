package HushCrypto;
import HushCrypto.CBCMode;
import HushCrypto.AES;
import HushCrypto.AES.AES_CTX;
import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.sound.sampled.*;
public class SteganographyUtils {

    private static final int BLOCK_SIZE = 16; // AES-128 block size

    // ---------- Encryption Method using Raw AES-128 CBC ----------
    public static byte[] encrypt(String plainText, String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");
        keyBytes = Arrays.copyOf(keyBytes, 16); 

        //random IV
        byte[] iv = new byte[BLOCK_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        //  plaintext to bytes and encrypt using CBCMode
        byte[] plaintextBytes = plainText.getBytes("UTF-8");
        byte[] ciphertext = CBCMode.encrypt(plaintextBytes, keyBytes, iv);

        // Prepend IV to ciphertext
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
        return result;
    }

    public static String decrypt(byte[] cipherBytes, String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");
        keyBytes = Arrays.copyOf(keyBytes, 16); 
    
        // Extract IV from cipherBytes
        byte[] iv = Arrays.copyOfRange(cipherBytes, 0, BLOCK_SIZE);
        byte[] actualCipherText = Arrays.copyOfRange(cipherBytes, BLOCK_SIZE, cipherBytes.length);
    
        try {
            byte[] decryptedBytes = CBCMode.decrypt(actualCipherText, keyBytes, iv);
            return new String(decryptedBytes, "UTF-8");
        } catch (IllegalArgumentException e) {
            // incorrect key
            throw new Exception("Decryption failed: Incorrect key or corrupted data.", e);
        }
    }
    

    // ---------- Audio Steganography Methods (Unchanged) ----------
    public static byte[] readAudioFile(File file) throws Exception {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioStream.getFormat();
        if (format.getSampleSizeInBits() != 16) {
            audioStream.close();
            throw new IllegalArgumentException("Audio file must be 16-bit PCM.");
        }
        byte[] audioBytes = audioStream.readAllBytes();
        audioStream.close();
        return audioBytes;
    }

    public static byte[] encodeMessage(byte[] audioBytes, byte[] messageBytes) throws Exception {
        int messageLength = messageBytes.length;
        String lengthBinary = String.format("%32s", Integer.toBinaryString(messageLength)).replace(' ', '0');
        StringBuilder messageBinary = new StringBuilder(lengthBinary);
        for (byte b : messageBytes) {
            String byteBinary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            messageBinary.append(byteBinary);
        }
        int totalBits = messageBinary.length();
        int bitIndex = 0;
        for (int i = 0; i < audioBytes.length - 1 && bitIndex < totalBits; i += 2) {
            int sample = ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF);
            int bitToEncode = messageBinary.charAt(bitIndex) - '0';
            sample = (sample & ~1) | bitToEncode;
            audioBytes[i] = (byte) (sample & 0xFF);
            audioBytes[i + 1] = (byte) ((sample >> 8) & 0xFF);
            bitIndex++;
        }
        if (bitIndex < totalBits) {
            throw new Exception("Message too long to encode in this audio file.");
        }
        return audioBytes;
    }

    public static byte[] decodeMessageBytes(byte[] audioBytes) throws Exception {
        StringBuilder lengthBinary = new StringBuilder();
        for (int i = 0; i < 64 && i < audioBytes.length - 1; i += 2) {
            int sample = ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF);
            int lsb = sample & 1;
            lengthBinary.append(lsb);
        }
        int messageLength = Integer.parseInt(lengthBinary.toString(), 2);
        int totalMessageBits = messageLength * 8;
        StringBuilder messageBinary = new StringBuilder();
        int bitsRead = 0;
        for (int i = 64; i < audioBytes.length - 1 && bitsRead < totalMessageBits; i += 2) {
            int sample = ((audioBytes[i + 1] & 0xFF) << 8) | (audioBytes[i] & 0xFF);
            int lsb = sample & 1;
            messageBinary.append(lsb);
            bitsRead++;
        }
        if (messageBinary.length() < totalMessageBits) {
            throw new Exception("Not enough data to decode the message.");
        }
        byte[] messageBytes = new byte[messageLength];
        for (int i = 0; i < messageLength; i++) {
            String byteStr = messageBinary.substring(i * 8, i * 8 + 8);
            messageBytes[i] = (byte) Integer.parseInt(byteStr, 2);
        }
        return messageBytes;
    }

    public static void writeAudioFile(byte[] audioBytes, File originalFile, File outputFile) throws Exception {
        AudioInputStream originalStream = AudioSystem.getAudioInputStream(originalFile);
        AudioFormat format = originalStream.getFormat();
        originalStream.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream modifiedStream = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());
        AudioSystem.write(modifiedStream, AudioFileFormat.Type.WAVE, outputFile);
        modifiedStream.close();
    }
}
