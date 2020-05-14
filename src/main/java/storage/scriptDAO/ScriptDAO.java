package storage.scriptDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads script from the memory and writes it to ArrayList.
 */
public class ScriptDAO implements IScriptDAO {
    private String path;

    public ScriptDAO(String path) {
        this.path = path;
    }

    @Override
    public List<String> getScript() throws IOException {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        List<String> scriptInput = new ArrayList<>();
        scriptInput.add(reader.readLine());

        while (reader.ready()) {
            String command = reader.readLine();

            scriptInput.add(command);
        }

        return scriptInput;
    }
}
