package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
                "1. Visa meddelanden\n" +
                "2. Lägg till meddelande\n" +
                "3. Uppdatera meddelande\n" +
                "4. Spara meddelanden till fil\n" +
                "5. Läs in meddelande från fil\n" +
                "6. Avsluta\n" +
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
        msgArr.forEach(Message -> System.out.println("Message: " + Message.message + "\n" +
                "Author: " + com.company.Message.author + "\n" +
                "Date: " + com.company.Message.createdAt + "\n" +
                "Last update: " + Message.updatedAt));
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

        for (int i = 0; i < msgArr.size(); i++) {
            JSONObject msg = new JSONObject();
            String message = msgArr.get(i).message;
            String author = msgArr.get(i).author;
            String createdAt = msgArr.get(i).createdAt;
            String updatedAt = msgArr.get(i).updatedAt;

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

            Iterator<JSONObject> iterator = jsonArr.iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next());

                String message = (String) iterator.next().get("message");
                String author = (String) iterator.next().get("author");
                String createdAt = (String) iterator.next().get("createdAt");
                String updatedAt = (String) iterator.next().get("updatedAt");

                System.out.println(message);
                Message jMsg = new Message(message);
                jMsg.author = author;
                jMsg.createdAt = createdAt;
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


    @Override
    public String toString() {
        return super.toString();
    }

    void update(String updateMsg) {
        this.setMessage(updateMsg);
        this.setUpdatedAt(new Date().toString());
    }
}