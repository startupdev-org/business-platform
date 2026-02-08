package com.platform.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugGenerator {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

    public static String generate(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String nfdNormalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String latinized = NONLATIN.matcher(nfdNormalized).replaceAll("");
        String slug = WHITESPACE.matcher(latinized).replaceAll("-");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");

        return slug.toLowerCase();
    }
}
