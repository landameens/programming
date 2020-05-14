package storage.scriptDAO;

import storage.exception.RecursionExeption;

import java.io.IOException;
import java.util.List;

/**
 * Interface for reading scripts in the memory.
 */
public interface IScriptDAO {
    List<String> getScript() throws IOException, RecursionExeption;
}
