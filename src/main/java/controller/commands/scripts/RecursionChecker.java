package controller.commands.scripts;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for checking recursion in execute_script command.
 * Stores all the hash codes of the executable scripts.
 */
public class RecursionChecker {
    private static Set<Integer> scripts = new HashSet<>();

    /**
     * Method for checking recursion using hash code.
     * If the hash code wasn't added to the collection, it is a recursion, since such an element already exists.
     * @param hashCode
     * @return is there a recursion
     */
    public boolean check(int hashCode){
        return scripts.add(hashCode);
    }

    public static void cleanRecursionChecker() {
        scripts = new HashSet<>();
    }
}
