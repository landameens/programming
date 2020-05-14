package controller.commands.scripts;

import java.util.List;
import java.util.Objects;

/**
 * Shell class for scripts.
 */
public class Script {
    private List<String> textScript;

    public List<String> getTextScript() {
        return textScript;
    }

    public void setTextScript(List<String> textScript) {
        this.textScript = textScript;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()){
            return false;
        }

        Script script = (Script) o;

        return Objects.equals(textScript, script.textScript);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textScript);
    }
}
