
package org.example;

public class ChatMember {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean isBot;
    private String languageCode;

    // Constructor
    public ChatMember(String userId, String username, String firstName, String lastName, boolean isBot) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBot = isBot;
        this.languageCode = "en"; // Default language
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isBot=" + isBot +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }

    public String getStatus() {
        return null;
    }
}
