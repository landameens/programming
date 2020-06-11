package controller.validators;


public final class PasswordEqualityValidator extends Validator {
    //private final LoggerAdapter loggerAdapter = LoggerAdapter.createDefault(PasswordEqualityValidator.class.getSimpleName());

    private static final String PASSWORDS_NOT_EQUAL = "The password and repeated password aren't equal.";

    @Override
    public String validateFields(String login, String password, String repeatedPassword) {
        if (password.equals(repeatedPassword)) {
           // loggerAdapter.info("PasswordEqualityValidator validates passwords SUCCESSFULLY.");
            return validateNext(login, password, repeatedPassword);
        }

       // loggerAdapter.info("PasswordEqualityValidator can't validate passwords: " + password + " & " + repeatedPassword);
        return PASSWORDS_NOT_EQUAL;
    }
}
