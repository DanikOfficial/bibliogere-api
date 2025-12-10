package com.pete.bibliogere.dto;

public record FetchUserQuestoesByUsernameResponse(String primeiraQuestao, String segundaQuestao, boolean firstLogin, boolean userActive) {
}
