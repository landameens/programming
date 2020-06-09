package domain.studyGroup.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.serverAdapter.ServerAdapter;
import controller.serverAdapter.exception.ServerAdapterException;
import controller.services.GsonPersonDeserializer;
import controller.services.GsonStudyGroupDeserializer;
import controller.services.JSONAdapter;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.person.Person;
import query.Query;
import response.Response;
import response.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerStudyGroupDAO {
    private ServerAdapter serverAdapter;
    private Gson gson = new GsonBuilder()
                                .registerTypeAdapter(StudyGroup.class, new GsonStudyGroupDeserializer())
                                .registerTypeAdapter(Person.class, new GsonPersonDeserializer())
                                .create();


    public ServerStudyGroupDAO(ServerAdapter serverAdapter) {
        this.serverAdapter = serverAdapter;
    }


    public StudyGroup create(StudyGroup studyGroup) throws ServerAdapterException {
        Map<String, String> map = new HashMap<>();
        map.put("xCoordinate", studyGroup.getCoordinates().getX() + "");
        map.put("yCoordinate", studyGroup.getCoordinates().getY() + "");
        map.put("groupAdminName", studyGroup.getGroupAdmin().getName());
        map.put("groupAdminPassportID", studyGroup.getGroupAdmin().getPassportID());
        map.put("groupAdminNationality", studyGroup.getGroupAdmin().getNationality().getName());
        map.put("groupAdminHeight", studyGroup.getGroupAdmin().getHeight() + "");
        map.put("studyGroupName", studyGroup.getName());
        map.put("studentsCount", studyGroup.getStudentsCount() + "");
        map.put("shouldBeExpelled", studyGroup.getShouldBeExpelled() + "");
        map.put("formOfEducation", studyGroup.getFormOfEducation().getName());
        map.put("semesterEnum", studyGroup.getSemesterEnum().getName());

        Query query = new Query("add", map);

        Response response = serverAdapter.send(query);

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            return gson.fromJson(response.getAnswer(), StudyGroup.class);
        }

        return null;
    }

    public List<StudyGroup> get() throws ServerAdapterException {
        Query query = new Query("getAllStudyGroups", new HashMap<>());

        Response response = serverAdapter.send(query);

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            return gson.fromJson(response.getAnswer(), new TypeToken<List<StudyGroup>>() {}.getType());
        }

        return null;
    }

    public StudyGroup update(StudyGroup studyGroup) throws ServerAdapterException {
        Map<String, String> map = new HashMap<>();
        map.put("id", studyGroup.getId() + "");
        map.put("xCoordinate", studyGroup.getCoordinates().getX() + "");
        map.put("yCoordinate", studyGroup.getCoordinates().getY() + "");
        map.put("groupAdminName", studyGroup.getGroupAdmin().getName());
        map.put("groupAdminPassportID", studyGroup.getGroupAdmin().getPassportID());
        map.put("groupAdminNationality", studyGroup.getGroupAdmin().getNationality().getName());
        map.put("groupAdminHeight", studyGroup.getGroupAdmin().getHeight() + "");
        map.put("studyGroupName", studyGroup.getName());
        map.put("studentsCount", studyGroup.getStudentsCount() + "");
        map.put("shouldBeExpelled", studyGroup.getShouldBeExpelled() + "");
        map.put("formOfEducation", studyGroup.getFormOfEducation().getName());
        map.put("semesterEnum", studyGroup.getSemesterEnum().getName());

        Query query = new Query("update", map);

        Response response = serverAdapter.send(query);

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            return gson.fromJson(response.getAnswer(), StudyGroup.class);
        }

        return null;
    }

    public void delete(int studyGroupId) throws ServerAdapterException {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(studyGroupId));

        Query query = new Query("remove_by_id", map);

        serverAdapter.send(query);
    }

}
