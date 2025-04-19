package com.smartvocab.smart_vocab_backend.util;

public class StringUtils {
    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
