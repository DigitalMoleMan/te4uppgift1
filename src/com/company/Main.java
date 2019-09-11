package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

class Main {
    private static final ArrayList<Message> msgArr = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
        mainMenu();
    }

    private static void mainMenu() throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Present the menu.
        System.out.println("--------------------------------\n" +
                "1. Show messages\n" +
                "2. Add new message\n" +
                "3. Update a message\n" +
                "4. Save messages to file\n" +
                "5. Read messages from file\n" +
                "6. Exit\n" +
                "--------------------------------");

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

    private static void showMessages() throws IOException, ParseException {
        msgArr.forEach(Message -> System.out.println(com.company.Message.author + " - " + Message.message+ " - " + com.company.Message.createdAt));
        mainMenu();
    }

    private static void addMessage() throws IOException, ParseException {
        int maxLength = 140;
        BufferedReader messageReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter message:");
        String messageInput = messageReader.readLine();
        if (messageInput.length() < maxLength) { //check if message exceeds the maximum amount of characters,
            Message message = new Message(messageInput);
            msgArr.add(message);
            System.out.println("Message: " + message.message + "\n" +
                    "Author: " + Message.author + "\n" +
                    "Date: " + Message.createdAt + "\n" +
                    "Last update: " + message.updatedAt);
        } else {
            System.out.println("Message has to be under 140 characters.");
        }
        mainMenu();
    }

    private static void updateMessage() throws IOException, ParseException {
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

    private static void saveMessages() throws IOException, ParseException {
        JSONArray list = new JSONArray();

        for (Message value : msgArr) {
            JSONObject msg = new JSONObject();
            String message = value.message;
            String author = Message.author;
            String createdAt = Message.createdAt;
            String updatedAt = value.updatedAt;

            msg.put("message", message);
            msg.put("author", author);
            msg.put("createdAt", createdAt);
            msg.put("updatedAt", updatedAt);

            list.add(msg);
        }
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(list.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(list);
        mainMenu();
    }

    private static void readMessages() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("messages.json")) {
            JSONArray jsonArr = (JSONArray) parser.parse((reader));

            for (JSONObject obj : (Iterable<JSONObject>) jsonArr) {

                String message = (String) obj.get("message");
                String author = (String) obj.get("author");
                String createdAt = (String) obj.get("createdAt");
                String updatedAt = (String) obj.get("updatedAt");

                Message jMsg = new Message(message);
                Message.author = author;
                Message.createdAt = createdAt;
                jMsg.updatedAt = updatedAt;

                msgArr.add(jMsg);
            }

        }
        mainMenu();
    }

    private static void exit() {
        System.exit(0);
    }
}

class Message {
    static String createdAt;
    String updatedAt;
    String message;
    static String author;

    Message(String Message) {
        createdAt = new Date().toString();
        updatedAt = new Date().toString();
        message = Message;
        author = System.getProperty("user.name");
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    void update(String updateMsg) {
        this.setMessage(updateMsg);
        this.setUpdatedAt(new Date().toString());
    }
}