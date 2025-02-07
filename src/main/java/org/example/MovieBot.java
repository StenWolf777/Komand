package org.example;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MovieBot extends TelegramLongPollingBot {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/kino_db";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "20091212";

    public MovieBot(String token) {
        createTableSql();
    }

    private void createTableSql() {

        String createTableSql = "CREATE TABLE f (\n" +
                "    id SERIAL PRIMARY KEY,\n" +
                "    file_id VARCHAR(255) NOT NULL,\n" +
                "    name VARCHAR(255) NOT NULL)";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            {
                statement.executeUpdate(createTableSql);
                System.out.println("photo jadvali yaratildi.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void Save_Video(String file_id) throws SQLException {
        String insertSQL = "INSERT INTO video (file_id) VALUES (?)";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, file_id);
            preparedStatement.executeUpdate();
            System.out.println("Vide Savaed ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getVideoById(int id) {
        String file_id = null;
        String selectSQL = "SELECT file_id FROM video WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    file_id = resultSet.getString("file_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return file_id;
    }

    @Override
    public String getBotUsername() {
        return "@Stepen_Wolf_bot";
    }

    @Override
    public String getBotToken() {
        return "7692374091:AAGVFHNw3TA1IX_KPhQH5Y_b830bHu5RLE8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasPhoto()) {
                PhotoSize photo = message.getPhoto().get(message.getPhoto().size() - 1);
                String fileId = photo.getFileId();
                try {
                    Save_Video(fileId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage(message.getChatId(), "Rasm muvaffaqiyatli saqlandi!");
            }

            if (message.hasText()) {
                try {
                    int photoId = Integer.parseInt(message.getText());
                    String fileId = getVideoById(photoId);
                    if (fileId != null) {
                        sendVideoById(message.getChatId(), fileId);

                    } else {
                        sendMessage(message.getChatId(), "Bunday ID bilan rasm topilmadi.");
                    }
                } catch (NumberFormatException e) {
                    sendMessage(message.getChatId(), "Iltimos, raqam kiriting.");
                }
            }


            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                Long chatId = update.getMessage().getChatId();


                if (text.equals("/start")) {

                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                    List<InlineKeyboardButton> row1 = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("Kanalga obuna bo'lish");
                    button1.setUrl("https://t.me/+PeTNBmZo2gZiZTZh");
                    row1.add(button1);
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("Tekshirish");
                    button2.setCallbackData("check");
                    row2.add(button2);

                    rowList.add(row1);
                    rowList.add(row2);

                    markup.setKeyboard(rowList);

                    SendMessage sendMessage = new SendMessage();
                    message.setText("Quyidagi kanalga obuna bo'ling");
                    message.getChatId();
                    message.setReplyMarkup(markup);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                if (data.equals("check")) {
                    GetChatMember member = new GetChatMember();
                    member.setChatId("-100227052739");
                    member.setUserId(update.getCallbackQuery().getMessage().getChatId());
                    ChatMember user = null;
                    try {
                        user = (ChatMember) execute(member);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    String status = user.getStatus();
                    if (status.equals("member") || status.equals("creator") || status.equals("administrator")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Obuna bo'lgansiz");
                        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        SendMessage sendmessage = new SendMessage();
                        sendmessage.setText("Obuna bo'lmagansiz");
                        sendmessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        try {
                            execute(sendmessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private void sendMessage(Long chatId, String s) {

    }

    private void sendVideoById(Long chatId, String fileId) {
        SendVideo sendVideo  = new SendVideo();
        sendVideo.setChatId(chatId.toString());
        sendVideo.setCaption(String.valueOf(new InputFile(fileId)));
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
