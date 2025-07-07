package com.vynqtalk.server.dto.response;

public class ChartData {
    private String date;
    private int activeUsers;
    private int newUsers;
    private int messages;

    public ChartData() {}
    public ChartData(String date, int activeUsers, int newUsers, int messages) {
        this.date = date;
        this.activeUsers = activeUsers;
        this.newUsers = newUsers;
        this.messages = messages;
    }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getActiveUsers() { return activeUsers; }
    public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
    public int getNewUsers() { return newUsers; }
    public void setNewUsers(int newUsers) { this.newUsers = newUsers; }
    public int getMessages() { return messages; }
    public void setMessages(int messages) { this.messages = messages; }
} 