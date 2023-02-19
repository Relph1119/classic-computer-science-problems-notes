package com.teapot.chapter1.onetime_pad;

import java.util.Random;

/**
 * P10~12
 * 牢不可破的加密方案：一次性密码本
 * 将无意义的随机假数据混入原始数据中，给加密程序配上密钥对，必须同时拥有两个密钥才能解密出原始数据
 * 使用XOR方式进行加密和解密：
 * C = A ^ B
 * A = C ^ B
 * B = C ^ A
 */
public class UnbreakableEncryption {
    /**
     * 生成随机密钥，作为密钥对的假数据
     * 假数据符合三个标准：假数据必须和原始数据长度相同、真正随机、完全保密
     */
    private static byte[] randomKey(int length) {
        byte[] dummy = new byte[length];
        Random random = new Random();
        random.nextBytes(dummy);
        return dummy;
    }

    public static KeyPair encrypt(String original) {
        byte[] originalBytes = original.getBytes();
        byte[] dummyKey = randomKey(originalBytes.length);
        byte[] encryptedKey = new byte[originalBytes.length];
        for (int i = 0; i < originalBytes.length; i++) {
            encryptedKey[i] = (byte) (originalBytes[i] ^ dummyKey[i]);
        }
        return new KeyPair(dummyKey, encryptedKey);
    }

    public static String decrypt(KeyPair kp) {
        byte[] decrypted = new byte[kp.key1.length];
        for (int i = 0; i < kp.key1.length; i++) {
            decrypted[i] = (byte) (kp.key1[i] ^ kp.key2[i]);
        }
        return new String(decrypted);
    }

    public static void main(String[] args) {
        KeyPair kp = encrypt("One Time Pad!");
        String result = decrypt(kp);
        System.out.println(result);
    }
}
