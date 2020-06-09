package domain.studyGroup.person;

public enum Country {
    UNITED_KINGDOM("united_kingdom"),
    GERMANY("germany"),
    VATICAN("vatican"),
    SOUTH_KOREA("south_korea"),
    JAPAN("japan");

    private String name;
    Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Country getCountry(String name){
        Country[] allCountries = Country.values();

        if (name == null){
            return null;
        }

        for (Country country : allCountries){
            if (name.equals(country.getName())){
                return country;
            }
        }

        return null;
    }
}
