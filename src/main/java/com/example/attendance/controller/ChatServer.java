package com.example.attendance.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chat")
public class ChatServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // メッセージを保持するリスト（全員で共有するようstaticにする）
    private static List<String> messages = new ArrayList<>();

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

    // POSTリクエストを処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // リクエストからパラメータ取得
        String user = request.getParameter("user");
        String msg = request.getParameter("msg");

        // メッセージ追加
        if (user != null && msg != null && !msg.trim().isEmpty()) {
            addMessage(user, msg);
        }

        // 画面に返す（JSONやHTMLなど用途に応じて変更可）
        response.setContentType("text/plain; charset=UTF-8");
        for (String m : getMessages()) {
            response.getWriter().println(m);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");

        // すべてのメッセージを返す
        for (String m : getMessages()) {
            response.getWriter().println(m);
        }
    }

    
    

}
