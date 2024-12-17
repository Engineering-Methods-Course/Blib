package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.commandMessage;
import logic.Subscriber;
import ocsf.server.*;

public class ServerController extends AbstractServer{

    private Connection conn;
    private commandMessage command;
    public ServerController(int port) {
        super(port);
    }
    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     * @param
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try
        {
            if(msg instanceof commandMessage)
            {
                this.command = (commandMessage) msg;

                /**
                 * 201 - Get info of a specific subscriber
                 * 203 - Edit info of a specific subscriber
                 * 205 - Get a list of all subscribers
                 */
                switch (command.getId()){
                    case 201:
                        ArrayList<String> subscriberDetails = displaySubscriberDetails((String)command.getMessageContent());
                        sendToAllClients(subscriberDetails); // change to send to client
                        break;
                    case 203:
                        ArrayList<String> editedDetails = editSubscriberDetails((String)command.getMessageContent());
                        sendToAllClients(editedDetails); // change to send to client
                        break;
                    case 205:
                        ArrayList<ArrayList<String>> subscribersList = getSubscribersList();
                        sendToAllClients(subscribersList); // change to send to client

                        break;
                    default:
                        System.out.println("Invalid command id");
                }
            }
        }catch (Exception e)
        {
            System.out.println("Error: incorrect msg object type" + e);
        }

    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted()
    {
        System.out.println ("Server listening for connections on port " + getPort());

        connectToDb();

    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped()  {
        System.out.println ("Server has stopped listening for connections.");
    }

    private void connectToDb()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            /* handle the error*/
            System.out.println("Driver definition failed");
        }

        try
        {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST","root","Aa123456");
            System.out.println("SQL connection succeed");
        } catch (SQLException ex)
        {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }










    /**
     * This method displays the details of a specific subscriber
     * @param messageContent
     */
    private ArrayList<String> displaySubscriberDetails(String messageContent) {
        ArrayList<String> subscriberDetails = new ArrayList<>();
        try {
            /**
             * The query selects all columns from the subscriber table where the id matches a given value
             * stmt.setString(1, messageContent) sets the first placeholder in the query to the value of messageContent
             */
            String query = "SELECT * FROM subscriber WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, messageContent);

            /**
             * The result set is the result of the query
             * The while loop iterates over the result set and prints the values of the columns
             */
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                subscriberDetails.add(rs.getString("subscriber_id"));
                subscriberDetails.add(rs.getString("subscriber_name"));
                subscriberDetails.add(rs.getString("subscriber_phone_number"));
                subscriberDetails.add(rs.getString("subscriber_email"));
                subscriberDetails.add(rs.getString("subscriber_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriberDetails;
    }

    /**
     * This method edits the details of a specific subscriber
     * @param messageContent
     */
    private ArrayList<String> editSubscriberDetails(String messageContent) {
        ArrayList<String> editedDetails = new ArrayList<>();
        try {
            /**
             * The message content is split into an array of strings using the regex pattern
             */
            String regex = "[,\\.\\s]";
            String[] details = messageContent.split(regex);
            String subscriberId = details[0];
            String subscriberName = details[1];
            String subscriberPhoneNumber = details[2];
            String subscriberEmail = details[3];

            /**
             * The query updates the name, phone number, and email of the subscriber where the id matches the given value
             */
            String query = "UPDATE subscriber SET subscriber_name = ?, subscriber_phone_number = ?, subscriber_email = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, subscriberName);
            statement.setString(2, subscriberPhoneNumber);
            statement.setString(3, subscriberEmail);
            statement.setString(4, subscriberId);

            /**
             *  Execute the update
             *  If the update was successful, retrieve the updated details
             */
            try
            {
                statement.executeUpdate();
                query = "SELECT * FROM subscriber WHERE id = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, subscriberId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    editedDetails.add(rs.getString("subscriber_id"));
                    editedDetails.add(rs.getString("subscriber_name"));
                    editedDetails.add(rs.getString("subscriber_phone_number"));
                    editedDetails.add(rs.getString("subscriber_email"));
                    editedDetails.add(rs.getString("subscriber_status"));
                }
            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editedDetails;
    }

    /**
     * This method retrieves a list of all subscribers
     */
    private ArrayList<ArrayList<String>> getSubscribersList() {
        ArrayList<ArrayList<String>> subscribersList = new ArrayList<>();
        try {
            /**
             * The query selects all columns from the subscriber table
             */
            String query = "SELECT * FROM subscriber";
            PreparedStatement statement = conn.prepareStatement(query);

            /**
             * Execute the query
             * If the query was successful, add the values of the columns to a list
             * Add the list to the subscribers list
             */
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ArrayList<String> subscriberDetails = new ArrayList<>();
                subscriberDetails.add(rs.getString("subscriber_id"));
                subscriberDetails.add(rs.getString("subscriber_name"));
                subscriberDetails.add(rs.getString("subscriber_phone_number"));
                subscriberDetails.add(rs.getString("subscriber_email"));
                subscriberDetails.add(rs.getString("subscriber_status"));
                subscribersList.add(subscriberDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribersList;
    }
}