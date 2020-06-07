package controller.services;

import controller.components.serviceMediator.Service;

import java.security.NoSuchAlgorithmException;

public interface PasswordHashService extends Service {
    String hash(String password) throws NoSuchAlgorithmException;
}
