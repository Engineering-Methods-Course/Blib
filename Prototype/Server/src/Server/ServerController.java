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

                        break;
                    case 205:

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
}
