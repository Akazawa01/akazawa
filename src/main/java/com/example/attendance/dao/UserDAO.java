package com.example.attendance.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.attendance.dto.User;

public class UserDAO {

    private static final Map<String, User> users = new HashMap<>();

    static {
        // Sample users with hashed passwords
        users.put("employee1", new User("employee1", hashPassword("password"), "employee", true));
        users.put("admin1", new User("admin1", hashPassword("adminpass"), "admin", true));
        users.put("employee2", new User("employee2", hashPassword("password"), "employee", true));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public boolean verifyPassword(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.isEnabled() && user.getPassword().equals(hashPassword(password));
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void updateUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void deleteUser(String username) {
        users.remove(username);
    }

    public void resetPassword(String username, String newPassword) {
        User user = users.get(username);
        if (user != null) {
            users.put(username, new User(user.getUsername(), hashPassword(newPassword), user.getRole(), user.isEnabled()));
        }
    }

    public void toggleUserEnabled(String username, boolean enabled) {
        User user = users.get(username);
        if (user != null) {
            users.put(username, new User(user.getUsername(), user.getPassword(), user.getRole(), enabled));
        }
    }

    // ★ static に変更
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
