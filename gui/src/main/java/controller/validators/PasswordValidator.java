package controller.validators;

import adapter.LoggerAdapter;
import org.apache.commons.configuration2.Configuration;

public final class PasswordValidator extends Validator {
    private final int minLength;
    private final int maxLength;

    private final LoggerAdapter loggerAdapter = LoggerAdapter.createDefault(PasswordValidator.class.getSimpleName());

    private static final String MIN_LENGTH = "user.password.minLength";
    private static final String MAX_LENGTH = "user.password.maxLength";
    private static final String INCORRECT_PASSWORD = "Password is incorrect. Allowed characters from 3 to 10";

    public PasswordValidator(Configuration configuration) {
        this.minLength = configuration.getInt(MIN_LENGTH);
        this.maxLength = configuration.getInt(MAX_LENGTH);
    }

    @Override
    public String validateFields(String login, String password, String repeatedPassword) {
        if (password.length() >= minLength && password.length() <= maxLength) {
            loggerAdapter.info("PasswordValidator validates password SUCCESSFULLY.");
            return validateNext(login, password, repeatedPassword);
        }

        loggerAdapter.info("PasswordValidator can't validate password:" + password);
        return INCORRECT_PASSWORD;
    }
}
