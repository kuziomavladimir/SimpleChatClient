package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Animations.Shake;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    public static final String VERSION = "0.1";


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Label label2;

    @FXML
    private Button loginSignInButton;

    @FXML
    private Label labelVersion;

    @FXML
    private Label labelPoweredBy;

    @FXML
    private Label label3;

    @FXML
    private Label label1;

    @FXML
    private Button SignUpButton;

    public ConnectionServerHandler cs;


    @FXML
    void initialize()
    {
        labelVersion.setText(labelVersion.getText() + VERSION);

        cs = new ConnectionServerHandler();
        label3.setText(cs.serverMessage);

        loginSignInButton.setOnAction(event -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            signInNewUser();
        });

        SignUpButton.setOnAction(event -> {
            System.out.println("вы нажали регистрация");
            SignUpButton.getScene().getWindow().hide();
            openNewScene("/sample/signUp.fxml");
        });
    }

    public void openNewScene(String window)
    {
        //SignUpButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        //stage.setTitle("SignUp");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void signInNewUser()
    {
        String email = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String nickname = "";
        User user = new User(email, password, nickname);

        cs.sendMessageForRegistration(user, "~~rezervStringForSignIn");

        try {
            Thread.sleep(1000); //Пофиксить с семафорами
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Отказ засыпания потока" + e);
        }
        System.out.println(cs.serverMessage);

        if(cs.serverMessage.equals("~~rezervStrindOpenProfile"))
        {
            cs.stopConnection();
            label3.setText("Welcome!");
            System.out.println(user.getNickName() + "Авторизован");
            SignUpButton.getScene().getWindow().hide();
            openNewScene("/sample/homePage.fxml");
        }
        else
        {
            Shake userLoginAnim = new Shake(loginField);
            Shake userPassAnim = new Shake(passwordField);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
            label3.setText(cs.serverMessage);

        }
    }
}