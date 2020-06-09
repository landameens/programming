package controller.services;

import com.google.gson.*;
import domain.exception.VerifyException;
import domain.studyGroup.person.Country;
import domain.studyGroup.person.Person;

import java.lang.reflect.Type;

public final class GsonPersonDeserializer implements JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int height = jsonObject.get("height").getAsInt();
        String passportId = jsonObject.get("passportId").getAsString();
        Country country = Country.getCountry(jsonObject.get("nationality").getAsString());

        try {
            return new Person(name, height, passportId, country);
        } catch (VerifyException e) {
            throw new JsonParseException(e);
        }
    }
}
