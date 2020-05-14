package domain.studyGroup;

public enum Semester {
    FIRST("first"),
    SECOND("second"),
    FOURTH("fourth"),
    EIGHTH("eighth");

    private String name;
    Semester(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Semester getSemesterEnum(String name){
        Semester[] semesters = Semester.values();

        for (Semester semester : semesters){
            if (name.equals(semester.getName())){
                return semester;
            }
        }

        return null;
    }
}
