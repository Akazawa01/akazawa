package com.example.attendance.controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.attendance.dao.UserDAO;
import com.example.attendance.dto.User;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        // ログインチェックと管理者権限チェック
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // セッションから成功メッセージを取得し、リクエストにセット
        String message = (String) session.getAttribute("successMessage");
        if (message != null) {
            req.setAttribute("successMessage", message);
            session.removeAttribute("successMessage");
        }

        // アクションに応じた処理
        if ("list".equals(action) || action == null) {
            // ユーザーリスト表示
            Collection<User> users = userDAO.getAllUsers();
            req.setAttribute("users", users);
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/user_management.jsp");
            rd.forward(req, resp);

        } else if ("edit".equals(action)) {
            // ユーザー編集画面表示
            String username = req.getParameter("username");
            User userToEdit = userDAO.findByUsername(username);
            req.setAttribute("userToEdit", userToEdit);
            Collection<User> users = userDAO.getAllUsers();
            req.setAttribute("users", users);
            RequestDispatcher rd = req.getRequestDispatcher("/jsp/user_management.jsp");
            rd.forward(req, resp);

        } else {
            // アクションがない場合はリストにリダイレクト
            resp.sendRedirect("users?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        // ログインチェックと管理者権限チェック
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            resp.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "add":
                handleAddUser(req, session);
                break;
            case "update":
                handleUpdateUser(req, session);
                break;
            case "delete":
                handleDeleteUser(req, session);
                break;
            case "reset_password":
                handleResetPassword(req, session);
                break;
            case "toggle_enabled":
                handleToggleEnabled(req, session);
                break;
            default:
                // 不正なアクションの場合
                session.setAttribute("errorMessage", "不正な操作です。");
                break;
        }

        resp.sendRedirect("users?action=list");
    }

    private void handleAddUser(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        if (userDAO.findByUsername(username) == null) {
            userDAO.addUser(new User(username, UserDAO.hashPassword(password), role, true));
            session.setAttribute("successMessage", "ユーザーを追加しました。");
        } else {
            session.setAttribute("errorMessage", "ユーザーIDは既に存在します。");
        }
    }

    private void handleUpdateUser(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String role = req.getParameter("role");
        boolean enabled = req.getParameter("enabled") != null;

        User existingUser = userDAO.findByUsername(username);
        if (existingUser != null) {
            userDAO.updateUser(new User(username, existingUser.getPassword(), role, enabled));
            session.setAttribute("successMessage", "ユーザー情報を更新しました。");
        }
    }

    private void handleDeleteUser(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        userDAO.deleteUser(username);
        session.setAttribute("successMessage", "ユーザーを削除しました。");
    }

    private void handleResetPassword(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        String newPassword = req.getParameter("newPassword");
        userDAO.resetPassword(username, UserDAO.hashPassword(newPassword)); // パスワードをハッシュ化
        session.setAttribute("successMessage", username + "のパスワードをリセットしました。");
    }

    private void handleToggleEnabled(HttpServletRequest req, HttpSession session) {
        String username = req.getParameter("username");
        User user = userDAO.findByUsername(username);

        if (user != null) {
            boolean newStatus = !user.isEnabled(); // 現在の状態を反転
            userDAO.toggleUserEnabled(username, newStatus);
            session.setAttribute("successMessage", username + "のアカウントを" + (newStatus ? "有効" : "無効") + "にしました。");
        }
    }
}