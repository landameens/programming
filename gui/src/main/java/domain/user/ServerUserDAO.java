package domain.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.serverAdapter.ServerAdapter;
import controller.serverAdapter.exception.ServerAdapterException;
import response.Response;
import response.Status;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerUserDAO {
    private final ServerAdapter serverAdapter;
    private final Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();


    public ServerUserDAO(ServerAdapter serverAdapter) {
        this.serverAdapter = serverAdapter;
    }

    public List<User> getAllUser() throws ServerAdapterException {
        Map<String, String> queryArguments = new HashMap<>();

        Response response = serverAdapter.send("getAllUsers", queryArguments);

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            return gson.fromJson(response.getAnswer(), new TypeToken<List<User>>() {}.getType());
        }

        return Collections.emptyList();
    }
}
