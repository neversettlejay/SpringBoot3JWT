package com.jaytech.security.cryptograpy.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateKeys {

    private KeyPairGenerator keyPairGenerator;
    private KeyGenerator keyGenerator;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKey secretKey;

    public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
//        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        this.keyPairGenerator.initialize(keylength);
//

        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(128); // The AES key size in number of bits
        this.secretKey = keyGenerator.generateKey();
    }
    public void createKeys() {
        this.keyPair = this.keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key);
        fos.flush();
        fos.close();
    }


    public String initializeKeys(int keyLength) {
        try {
            GenerateKeys generateKeys = new GenerateKeys(keyLength);
            generateKeys.createKeys();
            generateKeys.writeToFile("KeyPair/publicKey", generateKeys.getPublicKey().getEncoded());
            generateKeys.writeToFile("KeyPair/privateKey", generateKeys.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "public key and private key generated successfully";

    }


}