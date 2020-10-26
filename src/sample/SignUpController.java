package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField emailField1;

    @FXML
    private Label label3;

    @FXML
    private Label label2;

    @FXML
    private Label labelVersion;

    @FXML
    private Label labelPoweredBy;

    @FXML
    private Label label1;

    @FXML
    private Button backButton;

    public ConnectionServerHandler cs;

    @FXML
    void initialize()
    {
        labelVersion.setText(labelVersion.getText() + Controller.VERSION);

        cs = new ConnectionServerHandler();
        label3.setText(cs.serverMessage);

        signUpButton.setOnAction(event -> {
            signUpNewUser2();

        });

        backButton.setOnAction(event -> {
            cs.stopConnection();
            System.out.println("Закрыто");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Ошибка при засыпании после cs.stopConnection();" + e);
            }
            backButton.getScene().getWindow().hide();
            try {
                new Main().start(new Stage());
            } catch (Exception e) {
                System.out.println("Ошибка назад при открытии нового окна " + e);
            }
        });

    }

    private void signUpNewUser2()
    {
        String email = emailField1.getText().trim();
        String password = passwordField.getText().trim();
        String nickname = loginField.getText().trim();
        User user = new User(email, password, nickname);

        cs.sendMessageForRegistration(user, "~~rezervStringForRegistration");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Ошибка при засыпании потока в окне регистрации " + e);
        }
        label3.setText(cs.serverMessage);
    }
}
