package view.main;

import controller.localizer.Localizer;
import domain.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.annotation.Nullable;


public class ProfileController {
    @FXML
    private TextField loginField;

    private User user;

    @FXML
    private Label profileLabel;

    @FXML
    private Label loginLabel;

    public ProfileController(@Nullable User user) {
        this.user = user;
    }

    @FXML
    private void initialize() {
        /*Localizer.bindComponentToLocale(profileLabel, "ProfileScreen", "profile");
        Localizer.bindComponentToLocale(loginLabel, "ProfileScreen", "login");*/

        loginField.setText(user.getLogin());
    }
}
