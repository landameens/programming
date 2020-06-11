package controller.validators;

import org.apache.commons.configuration2.Configuration;

public final class LoginValidator extends Validator {
    private final int minLength;
    private final int maxLength;


    private static final String MIN_LENGTH = "user.login.minLength";
    private static final String MAX_LENGTH = "user.login.maxLength";
    private static final String INCORRECT_LOGIN = "Login is incorrect. Allowed characters from 3 to 10";

    public LoginValidator(Configuration configuration) {
        this.minLength = configuration.getInt(MIN_LENGTH);
        this.maxLength = configuration.getInt(MAX_LENGTH);
    }

    @Override
    public String validateFields(String login, String password, String repeatedPassword) {
        if (login.length() >= minLength && login.length() <= maxLength) {
            //loggerAdapter.info("LoginValidator validates login SUCCESSFULLY.");
            return validateNext(login, password, repeatedPassword);
        }

        //loggerAdapter.info("LoginValidator can't validate login:" + login);
        return INCORRECT_LOGIN;
    }
}
