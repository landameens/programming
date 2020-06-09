package controller.services;

import controller.components.serviceMediator.Service;

public interface JSONAdapter extends Service {
    String toJson(Object object);

    <T> T from(String json, Class<T> clazz);
}
