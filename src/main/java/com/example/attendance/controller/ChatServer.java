package com.example.attendance.controller;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/chat")

public class ChatServer {
    // メッセージを保持するリスト
    private List<String> messages;

    // コンストラクタ
    public ChatServer() {
        messages = new ArrayList<>();
    }

    // メッセージを追加する
    public void addMessage(String user, String msg) {
        String formatted = user + ": " + msg;
        messages.add(formatted);
    }

    // すべてのメッセージを取得する
    public List<String> getMessages() {
        return messages;
    }

    // コンソールに全部のメッセージを表示する
    public void printMessages() {
        System.out.println("----- チャット -----");
        for (String m : messages) {
            System.out.println(m);
        }
        System.out.println("------------------");
    }
}
