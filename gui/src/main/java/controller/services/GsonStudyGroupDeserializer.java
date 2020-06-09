package controller.services;

import com.google.gson.*;
import domain.exception.VerifyException;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.coordinates.Coordinates;
import domain.studyGroup.person.Person;

import java.lang.reflect.Type;
import java.nio.channels.ClosedByInterruptException;
import java.time.LocalDateTime;

public final class GsonStudyGroupDeserializer implements JsonDeserializer<StudyGroup> {
    @Override
    public StudyGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        Coordinates coordinates = context.deserialize(jsonObject.get("coordinates"), Coordinates.class);
        LocalDateTime creationDate = LocalDateTime.parse(jsonObject.get("creationDate").getAsString());
        int studentsCount = jsonObject.get("studentsCount").getAsInt();
        long shouldBeExpelled = jsonObject.get("shouldBeExpelled").getAsLong();
        FormOfEducation formOfEducation = FormOfEducation.getFormOfEducation(jsonObject.get("formOfEducation").getAsString());
        Semester semesterEnum = Semester.getSemesterEnum(jsonObject.get("semesterEnum").getAsString());
        Person groupAdmin = context.deserialize(jsonObject.get("groupAdmin"), Person.class);

        try {
            return new StudyGroup(id, name, coordinates, creationDate, studentsCount, shouldBeExpelled, formOfEducation, semesterEnum, groupAdmin);
        } catch (VerifyException e) {
            throw new JsonParseException(e);
        }
    }
}
