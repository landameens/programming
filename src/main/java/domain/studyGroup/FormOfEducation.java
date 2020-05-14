package domain.studyGroup;

public enum FormOfEducation {
    DISTANCE_EDUCATION("distance_education"),
    FULL_TIME_EDUCATION("full_time_education"),
    EVENING_CLASSES("evening_classes");

    private String name;
    FormOfEducation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FormOfEducation getFormOfEducation(String name){
        FormOfEducation[] formOfEducations = FormOfEducation.values();

        for (FormOfEducation formOfEducation : formOfEducations){
            if (name.equals(formOfEducation.getName())) {
                return formOfEducation;
            }
        }

        return null;
    }
}
