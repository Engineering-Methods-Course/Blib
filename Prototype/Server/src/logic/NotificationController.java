package logic;


import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NotificationController {

    private static NotificationController instance = null;
    private String from ;
    private String username;
    private String password;
    private final String host = "smtp.gmail.com";
    Properties properties = new Properties();
    Authenticator authenticator = null;

    public NotificationController() {
        Dotenv dotenv = Dotenv.load();
        from = dotenv.get("EMAIL_ADDRESS");
        username = dotenv.get("EMAIL_ADDRESS");
        password = dotenv.get("EMAIL_PASSWORD");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            System.out.println("NotificationController was created successfully");
            instance = new NotificationController();
        }
        return instance;
    }
    public void sendEmail(String recipient, String subject, String text) {

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText("Testing email");
            message.setContent("TEST 123", "text/html");
            System.out.println("Sending email...");
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.out.println("Error sending email"+ e.getMessage());

        }

    }

}