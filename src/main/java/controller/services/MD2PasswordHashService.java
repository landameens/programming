package controller.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD2PasswordHashService implements PasswordHashService {
    private final MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance("MD-2");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String hash(String password) throws NoSuchAlgorithmException {
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashedBytes = messageDigest.digest(bytes);
        return new String(hashedBytes, StandardCharsets.UTF_8);
    }
}
