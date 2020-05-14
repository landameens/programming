package domain.studyGroup.coordinates;

import domain.exception.VerifyException;

public class Coordinates implements Cloneable{
    private static final String EMPTY_VALUE = "Значение не должно быть пустым.";
    private static final String MAXIMUM_VALUE = "Превышено максимальное значение.";

    private Integer x; //Максимальное значение поля: 28, Поле не может быть null
    private int y;

    public Coordinates(Integer x, int y) throws VerifyException {
        checkX(x);
        this.x = x;
        this.y = y;
    }

    private void checkX(Integer x) throws VerifyException{
        if (x == null){
            throw new VerifyException(EMPTY_VALUE);
        }

        if (x > 28){
            throw new VerifyException(MAXIMUM_VALUE);
        }

    }

    public static Coordinates createCoordinates(CoordinatesDTO coordinatesDTO) throws VerifyException {
        Integer x = coordinatesDTO.x;
        int y = coordinatesDTO.y;

        return new Coordinates(x, y);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Coordinates getCoordinates(CoordinatesDTO coordinatesDTO) throws VerifyException {
        return new Coordinates(coordinatesDTO.x,
                coordinatesDTO.y);
    }

    public static CoordinatesDTO getCoordinatesDTO(Coordinates coordinates){
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();

        coordinatesDTO.x = coordinates.getX();
        coordinatesDTO.y = coordinates.getY();

        return coordinatesDTO;
    }

    @Override
    public Coordinates clone() {
        try {
            return new Coordinates(this.x,
                    this.y);
        } catch (VerifyException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public String toString() {
        return "x=" + x + "; " + "y=" + y;
    }
}
