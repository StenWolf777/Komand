package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.*;

public class MovieBot extends TelegramLongPollingBot {


    private final String BOT_USERNAME = "@Stepen_Wolf_bot";
    private final String BOT_TOKEN = "7692374091:AAGVFHNw3TA1IX_KPhQH5Y_b830bHu5RLE8";
    private final String CHANNEL_ID = "1002270527397";


    private final String DB_URL = "jdbc:postgresql://localhost:5432/kino";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "20091212";

    public MovieBot(String s) {
        createTable();
    }

    public MovieBot() {

    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String fileId = null;
            String fileName = null;

            if (message.hasDocument()) {
                fileId = message.getDocument().getFileId();
                fileName = message.getDocument().getFileName();
            } else if (message.hasVideo()) {
                fileId = message.getVideo().getFileId();
                fileName = "video.mp4";
            }

            if (fileId != null) {
                long userId = message.getFrom().getId();
                if (isUserSubscribed(userId)) {
                    saveMovie(fileId, fileName);
                    sendMessage(message.getChatId(), "Fayl saqlandi: " + fileName);
                } else {
                    sendMessage(message.getChatId(), "Kanalga obuna bo'ling: t.me/joinchat/CHANNEL_LINK");
                }
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserSubscribed(long userId) {
        return true;
    }

    private void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS movies (" +
                    "id SERIAL PRIMARY KEY, " +
                    "file_id TEXT NOT NULL, " +
                    "name TEXT NOT NULL)";
            statement.execute(createTableSQL);
            System.out.println("Jadval muvaffaqiyatli yaratildi.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveMovie(String fileId, String fileName) {
        String insertSQL = "INSERT INTO movies (file_id, name) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, fileId);
            preparedStatement.setString(2, fileName);
            preparedStatement.executeUpdate();
            System.out.println("Ma'lumot saqlandi: " + fileName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
