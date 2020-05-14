package domain.commandsRepository;

public class Record {
    public String name;

    @Override
    public String toString() {
        return name + System.lineSeparator();
    }
}
