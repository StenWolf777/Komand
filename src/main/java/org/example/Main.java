package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        System.out.println("Bot muvoffaqiyatli ishga tushdi");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MovieBot("7692374091:AAGVFHNw3TA1IX_KPhQH5Y_b830bHu5RLE8"));
        System.out.println("successful");
    }
}
