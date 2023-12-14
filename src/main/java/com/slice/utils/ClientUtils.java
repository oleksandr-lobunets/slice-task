package com.slice.utils;

import java.util.Base64;


public class ClientUtils {

    private ClientUtils() {
    }

    public static String decodeBase64(String encoded) {
        Base64.Decoder decoder = Base64.getDecoder();
        // avoid java.lang.IllegalArgumentException: Illegal base64 character a
        String noNewlines = encoded.replace("\n", "");
        return new String(decoder.decode(noNewlines));
    }

}

