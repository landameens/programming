package controller.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.person.Person;

public final class JSONGsonAdapter implements JSONAdapter {
    private final Gson gson;


    public JSONGsonAdapter() {
        this.gson = new GsonBuilder()
                            .registerTypeAdapter(StudyGroup.class, new GsonStudyGroupDeserializer())
                            .registerTypeAdapter(Person.class, new GsonPersonDeserializer())
                            .create();
    }


    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * @throws com.google.gson.JsonSyntaxException if json is invalid
     */
    @Override
    public <T> T from(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
