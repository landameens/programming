package controller.services;

import com.google.gson.*;
import domain.studyGroup.StudyGroup;

import java.lang.reflect.Type;

public final class GsonStudyGroupSerializer implements JsonSerializer<StudyGroup> {
    @Override
    public JsonElement serialize(StudyGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(src.getId()));
        jsonObject.add("userId", new JsonPrimitive(src.getUserId()));
        jsonObject.add("name", new JsonPrimitive(src.getName()));
        jsonObject.add("coordinates", context.serialize(src.getCoordinates()));
        jsonObject.add("creationDate", new JsonPrimitive(src.getCreationDate().toString()));
        jsonObject.add("studentsCount", new JsonPrimitive(src.getStudentsCount()));
        jsonObject.add("shouldBeExpelled", new JsonPrimitive(src.getShouldBeExpelled()));
        jsonObject.add("formOfEducation", new JsonPrimitive(src.getFormOfEducation().getName()));
        jsonObject.add("semesterEnum", new JsonPrimitive(src.getSemesterEnum().getName()));
        jsonObject.add("groupAdmin", context.serialize(src.getGroupAdmin()));

        return jsonObject;
    }
}
