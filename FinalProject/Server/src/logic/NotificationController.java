package logic;


import common.ClientServerMessage;
import io.github.cdimascio.dotenv.Dotenv;
import ocsf.server.ConnectionToClient;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationController {

    private static final HashMap<Integer, ConnectionToClient> subscriberClients = new HashMap<>();
    private static final HashMap<Integer, ConnectionToClient> librarianClients = new HashMap<>();
    private static volatile NotificationController instance;
    private static DBController dbController;
    private final String from;
    private final String username;
    private final String password;
    private final String host;
    Properties properties = new Properties();
    Authenticator authenticator;

    /**
     * Constructor to initialize the NotificationController object.
     */
    private NotificationController() {
        /*
         * Load the .env file to get the email address and password
         */
        Dotenv dotenv = Dotenv.load();
        from = dotenv.get("EMAIL_ADDRESS");
        username = dotenv.get("EMAIL_ADDRESS");
        password = dotenv.get("EMAIL_PASSWORD");
        host = "smtp.gmail.com";

        /*
         * Set the properties for the email
         */
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        /*
         * Create an authenticator object to authenticate the email
         */
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
            synchronized (NotificationController.class) {
                if (instance == null) {
                    instance = new NotificationController();
                    dbController = DBController.getInstance();
                    System.out.println("NotificationController was created successfully");
                }
            }
        }
        return instance;
    }

    /**
     * This method sends a message to the librarian
     *
     * @param message the message to send
     */
    public static void notifyLibrarian(String message) {
        if (librarianClients.isEmpty()) {

            /*
             * Update the database with the message
             */
            dbController.messagesToLibrarian(message, 0);
            return;
        }

        /*
         * Send the message to all the librarian clients
         */
        for (ConnectionToClient client : librarianClients.values()) {
            try {
                dbController.messagesToLibrarian(message, 1);
                client.sendToClient(new ClientServerMessage(107, message));
            } catch (Exception e) {
                System.out.println("Error sending message to librarian: " + e);
            }
        }
    }

    /**
     * This method sends an email to the recipient
     *
     * @param recipientEmail the recipient of the email
     * @param subject        the subject of the email
     * @param text           the text of the email
     */
    public void sendEmail(String recipientEmail, int recipientID, String subject, String text) {

        Session session = Session.getInstance(properties, authenticator);
        try {
            if (!isValidEmailAddress(recipientEmail)) {
                throw new RuntimeException("Invalid email address: " + recipientEmail);
            }
            dbController.messagesToSubscriber(recipientID, text, 1);
            Message message = new MimeMessage(session); // Create a new email message
            message.setFrom(new InternetAddress(from)); // Set the email sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail)); // Set the email recipient
            message.setSubject(subject); // Set the email subject
            message.setContent(text, "text/html"); // Set the email body type
            System.out.println("Sending email...");
            Transport.send(message); // Send the email
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.out.println("Error sending email" + e.getMessage());
            throw new RuntimeException(e);

        }
    }

    /**
     * This method simulates sending an SMS to the recipient
     *
     * @param subscriberId the recipient of the email
     * @param message      the text of the email
     */
    public void sendSMSSimulator(int subscriberId, String message) {

        /*
         * Check if the subscriber is connected
         */
        if (subscriberClients.containsKey(subscriberId)) {
            try {

                /*
                 * Send the message to the subscriber
                 */
                subscriberClients.get(subscriberId).sendToClient(new ClientServerMessage(107, message));
                dbController.messagesToSubscriber(subscriberId, message, 1);
            } catch (Exception e) {
                System.out.println("Error sending SMS: " + e);
            }
        } else {

            /*
             * Update the database with the message
             */
            dbController.messagesToSubscriber(subscriberId, message, 0);
        }
    }

    /**
     * This method checks if the email address is valid
     *
     * @param email the email address
     * @return true if the email address is valid, false otherwise
     */
    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * This method adds the subscriber client to the list of subscriber clients
     *
     * @param subscriberID the subscriber ID
     * @param client       the client
     */
    public void addSubscriberClients(int subscriberID, ConnectionToClient client) {

        /*
         * Add the subscriber client to the list of subscriber clients
         */
        subscriberClients.put(subscriberID, client);
        ArrayList<String> messages = dbController.checkSubscriberMessages(subscriberID);
        if (messages == null) {
            return;
        }

        /*
         * Send the messages to the subscriber
         */
        for (String message : messages) {
            try {
                client.sendToClient(new ClientServerMessage(107, message));
            } catch (Exception e) {
                System.out.println("Error sending message to subscriber: " + e);
            }
        }
    }

    /**
     * This method adds the librarian client to the list of librarian clients
     *
     * @param librarianID the librarian ID
     * @param client      the client
     */
    public void addLibrarianClients(int librarianID, ConnectionToClient client) {

        /*
         * Add the librarian client to the list of librarian clients
         */
        librarianClients.put(librarianID, client);
        ArrayList<String> messages = dbController.checkLibrarianMessages();

        /*
         * Send the messages to the librarian
         */
        for (String message : messages) {
            try {
                client.sendToClient(new ClientServerMessage(107, message));
            } catch (Exception e) {
                System.out.println("Error sending message to librarian: " + e);
            }
        }

    }

    /**
     * This method removes the subscriber client from the list of subscriber clients
     *
     * @param subscriberID the subscriber ID
     */
    public void removeSubscriberClients(int subscriberID) {
        subscriberClients.remove(subscriberID);
    }

    /**
     * This method removes the librarian client from the list of librarian clients
     *
     * @param librarianID the librarian ID
     */
    public void removeLibrarianClients(int librarianID) {
        librarianClients.remove(librarianID);
    }

}