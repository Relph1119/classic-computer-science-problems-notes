package com.teapot.chapter1.onetime_pad;

/**
 * P11
 * 密钥对
 */
public class KeyPair {
    public final byte[] key1;
    public final byte[] key2;

    KeyPair(byte[] key1, byte[] key2) {
        this.key1 = key1;
        this.key2 = key2;
    }
}
