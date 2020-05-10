package ro.certificate.manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class AESUtils {

    @Autowired
    private ConfigurationUtils configurationUtils;

    private static final String AES_ALGORITHM_NAME = "AES";

    public String encryptKeyStorePassword(String data) throws Exception {
        return encrypt(data, configurationUtils.getKeyStoreEncryptionKey());
    }

    public String decryptKeyStorePassword(String encryptedData) throws Exception {
        return decrypt(encryptedData, configurationUtils.getKeyStoreEncryptionKey());
    }

    public String encryptPrivateKeyPassword(String data) throws Exception {
        return encrypt(data, configurationUtils.getPrivateKeyEncryptionKey());
    }

    public String decryptPrivateKeyPassword(String encryptedData) throws Exception {
        return decrypt(encryptedData, configurationUtils.getPrivateKeyEncryptionKey());
    }

    public String encryptCertificateID(String data) throws Exception {
        return encrypt(data, configurationUtils.getCertificateIDEncryptionKey());
    }

    public String decryptCertificateID(String encryptedData) throws Exception {
        return decrypt(encryptedData, configurationUtils.getCertificateIDEncryptionKey());
    }

    private String encrypt(String data, String encryptionKey) throws Exception {
        if (data != null && encryptionKey != null) {
            Key key = generateKey(encryptionKey);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encVal);
        }

        throw new IllegalArgumentException("Invalid arguments.");
    }

    private String decrypt(String encryptedData, String encryptionKey) throws Exception {
        if (encryptedData != null && encryptionKey != null) {
            Key key = generateKey(encryptionKey);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = cipher.doFinal(decodedValue);
            return new String(decValue);
        }

        throw new IllegalArgumentException("Invalid arguments.");
    }

    private Key generateKey(String encryptionKey) {
        return new SecretKeySpec(encryptionKey.getBytes(), AES_ALGORITHM_NAME);
    }
}
