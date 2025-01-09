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
    private String host;
    Properties properties = new Properties();
    Authenticator authenticator = null;
    /**
     * Constructor to initialize the NotificationController object.
     */
    public NotificationController() {
        // Load the .env file to get the email address and password
        Dotenv dotenv = Dotenv.load();
        from = dotenv.get("EMAIL_ADDRESS");
        username = dotenv.get("EMAIL_ADDRESS");
        password = dotenv.get("EMAIL_PASSWORD");
        host = "smtp.gmail.com";
        // Set the properties for the email
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        // Create an authenticator object to authenticate the email
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }

    /**
     * This method creates a new instance of NotificationController if it doesn't exist
     *
     * @return the instance of NotificationController
     */
    public static NotificationController getInstance() {
        if (instance == null) {
            System.out.println("NotificationController was created successfully");
            instance = new NotificationController();
        }
        return instance;
    }
    /**
     * This method sends an email to the recipient
     *
     * @param recipient the recipient of the email
     * @param subject the subject of the email
     * @param text the text of the email
     */
    public void sendEmail(String recipient, String subject, String text) {

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject); // Set the email subject
            message.setContent(text, "text/html"); // Set the email body type
            System.out.println("Sending email...");
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.out.println("Error sending email"+ e.getMessage());
            throw new RuntimeException(e);

        }

    }

}