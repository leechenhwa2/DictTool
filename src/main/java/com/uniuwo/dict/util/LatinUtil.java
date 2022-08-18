package com.uniuwo.dict.util;

import java.text.Normalizer;

public class LatinUtil {

    private static String normalize(String input) {
        return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
    }

    /**
     * eg. convert 'á' to 'a'.
     *
     * @param input
     * @return
     */
    public static String removeAccents(String input) {
        return normalize(input).replaceAll("\\p{M}", "");
    }

}
