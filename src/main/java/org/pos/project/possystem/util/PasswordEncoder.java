package org.pos.project.possystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordEncoder {

    private PasswordEncoder() {
    }

    public static String encodePassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String enteredPassword, String storedHash) {
        String enteredPasswordHash = encodePassword(enteredPassword);

        assert enteredPasswordHash != null;
        return enteredPasswordHash.equals(storedHash);
    }
}
