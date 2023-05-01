package me.l2x9.core.chat;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

/**
 * @author 254n_m
 * @since 2023/04/29 11:04 PM
 * This file was created as a part of L2X9RebootCore
 */
public interface Translator {
    CompletableFuture<String> translate(String toTranslate, String targetLang, boolean safeFail);

    CompletableFuture<String> translate(String toTranslate, String sourceLang, String targetLang, boolean safeFail);

    CompletableFuture<String> detectLanguage(String str);

    CompletableFuture<Boolean> checkFunctionality();
    HashSet<String> getSupportedLanguages();
}
