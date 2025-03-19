package HushCrypto;

import java.util.Arrays;

import HushCrypto.AES;
import HushCrypto.AES.AES_CTX;

public class CBCMode {
    private static final int BLOCK_SIZE = 16;

    // PKCS#5 Padding
    public static byte[] pad(byte[] data) {
        int padLen = BLOCK_SIZE - (data.length % BLOCK_SIZE);
        byte[] padded = Arrays.copyOf(data, data.length + padLen);
        Arrays.fill(padded, data.length, padded.length, (byte) padLen);
        return padded;
    }

    // Remove PKCS#5 padding.
    public static byte[] unpad(byte[] data) {
        int padLen = data[data.length - 1] & 0xFF;
        if (padLen < 1 || padLen > BLOCK_SIZE) {
            throw new IllegalArgumentException("Invalid padding");
        }

        for (int i = data.length - padLen; i < data.length; i++) { // last padLen bytes equals padLen.
            if ((data[i] & 0xFF) != padLen) {
                throw new IllegalArgumentException("Invalid padding");
            }
        }
        return Arrays.copyOf(data, data.length - padLen);
    }

    // Encrypt plaintext with PKCS#5 padding using AES-128 in CBC mode.
    // key and iv are both 16-byte arrays.
    public static byte[] encrypt(byte[] plaintext, byte[] key, byte[] iv) {
        byte[] padded = pad(plaintext);
        // Initialize AES context (this performs key expansion and sets IV)
        AES_CTX ctx = new AES_CTX(key, iv);
        byte[] ciphertext = new byte[padded.length];

        byte[] currentIV = Arrays.copyOf(iv, BLOCK_SIZE);

        for (int i = 0; i < padded.length; i += BLOCK_SIZE) {
            byte[] block = Arrays.copyOfRange(padded, i, i + BLOCK_SIZE);

            for (int j = 0; j < BLOCK_SIZE; j++) {
                block[j] ^= currentIV[j];
            }

            byte[] encryptedBlock = new byte[BLOCK_SIZE];
            AES.AES_Encrypt(ctx, block, encryptedBlock);

            System.arraycopy(encryptedBlock, 0, ciphertext, i, BLOCK_SIZE);
            currentIV = encryptedBlock;
        }
        return ciphertext;
    }

    // Decrypt ciphertext using AES-128 in CBC mode.
    // key and iv are both 16-byte arrays.
    public static byte[] decrypt(byte[] ciphertext, byte[] key, byte[] iv) {
        AES_CTX ctx = new AES_CTX(key, iv);
        AES.AES_DecryptInit(ctx, key, iv);

        byte[] padded = new byte[ciphertext.length];
        byte[] currentIV = Arrays.copyOf(iv, BLOCK_SIZE);

     
        for (int i = 0; i < ciphertext.length; i += BLOCK_SIZE) {
            byte[] block = Arrays.copyOfRange(ciphertext, i, i + BLOCK_SIZE);
            byte[] decryptedBlock = new byte[BLOCK_SIZE];

      
            AES.AES_Decrypt(ctx, block, decryptedBlock);

         
            for (int j = 0; j < BLOCK_SIZE; j++) {
                decryptedBlock[j] ^= currentIV[j];
            }


            System.arraycopy(decryptedBlock, 0, padded, i, BLOCK_SIZE);
  
            currentIV = block;
        }

        return unpad(padded);
    }
}
