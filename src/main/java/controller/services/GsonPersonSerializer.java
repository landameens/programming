package controller.services;

import com.google.gson.*;
import domain.studyGroup.person.Person;

import java.lang.reflect.Type;

public final class GsonPersonSerializer implements JsonSerializer<Person> {
    @Override
    public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("name", new JsonPrimitive(src.getName()));
        jsonObject.add("height", new JsonPrimitive(src.getHeight()));
        jsonObject.add("passportId", new JsonPrimitive(src.getPassportID()));
        jsonObject.add("nationality", new JsonPrimitive(src.getNationality().toString()));

        return jsonObject;
    }
}
