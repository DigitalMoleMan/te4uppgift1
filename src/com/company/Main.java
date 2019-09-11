package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

class Main {
    private static final ArrayList<Message> msgArr = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        mainMenu();
    }

    private static void mainMenu() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Present the menu.
        System.out.println("1. Visa meddelanden\n" +
                "2. Lägg till meddelande\n" +
                "3. Uppdatera meddelande\n" +
                "4. Spara meddelanden till fil\n" +
                "5. Läs in meddelande från fil\n" +
                "6. Avsluta");

        String menuInput = br.readLine(); //User input for the menu.

        switch (menuInput) {
            case "1":
                showMessages();
                break;
            case "2":
                addMessage();
                break;
            case "3":
                updateMessage();
                break;
            case "4":
                saveMessages();
                break;
            case "5":
                readMessages();
                break;
            case "6":
                exit();
                break;
            default: {
                System.out.println("Invalid input");
                mainMenu();
                break;
            }
        }

    }

    private static void showMessages() throws IOException {
        msgArr.forEach(Message -> System.out.println("Message: " + Message.message + "\n" +
                "Author: " + com.company.Message.author + "\n" +
                "Date: " + com.company.Message.createdAt + "\n" +
                "Last update: " + Message.updatedAt));
        mainMenu();
    }

    private static void addMessage() throws IOException {
        int maxLength = 140;
        BufferedReader messageReader = new BufferedReader(new InputStreamReader(System.in));
        String messageInput = messageReader.readLine();
        if (messageInput.length() < maxLength) { //check if message exceeds the maximum amount of characters,
            Message message = new Message(messageInput);
            msgArr.add(message);
            System.out.println(msgArr.toString());
        } else {
            System.out.println("Message has to be under 140 characters.");
        }
        mainMenu();
    }

    private static void updateMessage() throws IOException {
        for (int i = 0; i < msgArr.size(); i++) {
            System.out.println("[" + i + "]\n" +
                    "Message: " + msgArr.get(i).message + "\n" +
                    "Author: " + Message.author + "\n" +
                    "Date: " + Message.createdAt + "\n" +
                    "Last update: " + msgArr.get(i).updatedAt);
        }
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(System.in));
        int updateIndex = Integer.parseInt(updateReader.readLine());

        System.out.println("Old message: " + msgArr.get(updateIndex).message);
        System.out.println("Enter the new message");
        msgArr.get(updateIndex).update(updateReader.readLine());
        mainMenu();
    }

    private static void saveMessages() throws IOException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("messages.json"));
        pw.println("[");
        for (int i = 0; i < msgArr.size(); i++) {
            pw.println("{");
            pw.println("\"message\": \"" + msgArr.get(i).message + "\",");
            pw.println("\"author\": \"" + Message.author + "\",");
            pw.println("\"createdAt\": \"" + Message.createdAt + "\",");
            pw.println("\"updatedAt\": \"" + msgArr.get(i).updatedAt + "\"");
            if (i < msgArr.size() - 1) pw.println("},");
            else pw.println("}");
        }
        pw.println("]");
        pw.close();
        mainMenu();
    }

    private static void readMessages() throws IOException {

        mainMenu();
    }

    private static void exit() {
        System.exit(0);
    }
}

class Message {
    static Date createdAt;
    Date updatedAt;
    String message;
    static String author;

    Message(String Message) {
        createdAt = new Date();
        updatedAt = new Date();
        message = Message;
        author = System.getProperty("user.name");
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    void update(String updateMsg) {
        this.setMessage(updateMsg);
        this.setUpdatedAt(new Date());
    }
}