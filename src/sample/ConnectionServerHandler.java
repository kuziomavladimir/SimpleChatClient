package sample;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionServerHandler
{
    private String hostName = "127.0.0.1";
    private String portNum = "5000";
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket sock;

    private Thread readerThread;
    private Thread connectionThread;
    private CheckConnection check;

    private TextArea tArea;
    private TextField sendField;

//    private TextField emailField;
//    private TextField passwordField;
//    private TextField nicknameField;
    private User user;
    public String serverMessage;


    public ConnectionServerHandler(TextArea tArea, TextField sendField)
    {
        //Соединение для переписки
        this.tArea = tArea;
        this.sendField = sendField;

        check = new CheckConnection();
        connectionThread = new Thread(check);
        connectionThread.setDaemon(true); //свойство потока останавливаться при закрытии программы
        connectionThread.start();
    }

    public ConnectionServerHandler()
    {
        setUpNetworkingForRegistration();
    }

    public void setUpNetworkingForRegistration()
    {
        try {
            sock = new Socket(hostName, Integer.parseInt(portNum));
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedReader(streamReader);

            readerThread = new Thread(new answerRegistrationReader());
            readerThread.setDaemon(true);
            readerThread.start();

            System.out.println("Соединение установлено");
        } catch (Exception e)
        {
            System.out.println("Нет соединения");
            serverMessage = "No Connection";
            if(sock == null) System.out.println("sok = NULL!!!");
            sock = null;
            reader = null;
            writer = null;
            if(readerThread != null)
            {
                System.out.println(readerThread.isInterrupted());
                readerThread.interrupt();
                System.out.println(readerThread.isInterrupted());
                readerThread = null;
            }
            //e.printStackTrace();
        }
    }

    public void sendMessageForRegistration(User user, String rezervText)
    {
        this.user = user;
        try
        {
            String s = rezervText + "," + user.geteMail() + "," + user.getPassword() + "," + user.getNickName();
            writer.println(s);
            writer.flush();
            System.out.println("Отправляем на сервер: " + s);
            serverMessage = "You are registered successfully!!!";

        } catch (Exception e)
        {
            System.out.println("Исключение при записи" + e);
            serverMessage = "Error!";
        }
    }

    public class answerRegistrationReader implements Runnable
    {
        public void run()
        {
            try
            {
                while ((serverMessage = reader.readLine()) != null)
                {
                    System.out.println("Принимаем Регистрацию от сервера: " + serverMessage);
                }
            } catch (Exception ex)
            {
                System.out.println("Исключение при чтении");
                serverMessage = "Соединение прервано";
                writer = null;
                sock = null;
            }
        }
    }

    public void setUpNetworking()
    {
        try {
            sock = new Socket(hostName, Integer.parseInt(portNum));
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());

            readerThread = new Thread(new IncomingReader());
            readerThread.setDaemon(true);
            readerThread.start();
            tArea.appendText("Соединение установлено\n");
        } catch (Exception ex) {
            if(sock == null) System.out.println("sok = NULL!!!");
            sock = null;
            reader = null;
            writer = null;
            tArea.appendText("Нет соединения\n");
            if(readerThread != null)
            {
                System.out.println(readerThread.isInterrupted());
                readerThread.interrupt();
                System.out.println(readerThread.isInterrupted());
                readerThread = null;
            }
        }
    }

    public class CheckConnection implements Runnable
    {
        private boolean isActive;
        public CheckConnection()
        {
            isActive = true;
        }
        public void run()
        {
            while(isActive)
            {
                System.out.println("Проверяем соединение");

                if((sock == null)/* || (hostNameField.getText().equals(hostName) != true) ||
                        portNumField.getText().equals(portNum) != true*/)
                {
//                    hostName = hostNameField.getText();
//                    portNum = portNumField.getText();
                    setUpNetworking();
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex)
                {
                    break;
                }
            }
        }
    }

    public class IncomingReader implements Runnable
    {
        public void run()
        {
            String message;
            try
            {
                while ((message = reader.readLine()) != null)
                {
                    System.out.println("Принимаем от сервера: " + message);
                    tArea.appendText(message + "\n");
                }
            } catch (Exception ex)
            {
                System.out.println("Исключение при чтении");
                tArea.appendText("Соединение прервано\n");
                writer = null;
                sock = null;
            }
        }
    }

    public void sendMessage()
    {
        try
        {
            writer.println(sendField.getText());
            writer.flush();
            System.out.println("Отправляем на сервер: " + sendField.getText());

        } catch (Exception ex)
        {
            System.out.println("Исключение при записи");
            tArea.appendText("Не отправлено\n");
            sock = null;
        }
        sendField.setText("");
        sendField.requestFocus();
    }

    public void stopConnection() {

        try {
            check.isActive = false;
        } catch (Exception e)
        {
            System.out.println("check is null");
        }

        try
        {
            readerThread.interrupt();
            System.out.println("readerThread ПРЕРВАН?" + readerThread.isInterrupted());
            readerThread = null;
        } catch (NullPointerException e) {
            //e.printStackTrace();
            System.out.println("readerThread и так нулевой " + e);
        }

        try {
            connectionThread.interrupt();
            System.out.println("connectionThread ПРЕРВАН?" + connectionThread.isInterrupted());
            connectionThread = null;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("connectionThread и так нулевой " + e);

        }

        hostName = null;
        portNum = null;
        try {
            sock.close();
            reader.close();
            writer.close();
            check = null;
        } catch (Exception e) {
            System.out.println("Ошибка при закрытии sock или reader или writer " + e);
        }

        //System.gc();
    }



}

