package sample;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label label1;

    @FXML
    private AnchorPane imageBHome;

    @FXML
    private Button SendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    public TextArea textArea1;

    @FXML
    private ListView<?> usersListView;

    @FXML
    private Button logOutButton;

    public ConnectionServerHandler cs;


    @FXML
    void initialize()
    {
        System.out.println("Initialise");
        cs = new ConnectionServerHandler(textArea1, messageTextField);

        SendButton.setOnAction(event -> {
            cs.sendMessage();
        });

        logOutButton.setOnAction(event -> {
            cs.stopConnection();
            logOutButton.getScene().getWindow().hide();
            try {
                new Main().start(new Stage());
            } catch (Exception e) {
                System.out.println("Отказ при открытии нового окна" + e);
            }
        });
    }

}
