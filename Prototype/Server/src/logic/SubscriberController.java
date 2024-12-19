package logic;

import common.Subscriber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;

/**
 * This interface is used to control the subscriber
 */
public interface SubscriberController {
    /**
     * This method logins the subscriber to the system
     * and displays the details of a specific subscriber
     *
     * @param messageContent The message content
     * @param conn           The connection to the database
     * @return The subscriber
     */
    default Subscriber subscriberLogin(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> subscriberDetails = new ArrayList<>();
        try {
            /*
             * The query selects all columns from the subscriber table where the id matches a given value
             * stmt.setString(1, messageContent) sets the first placeholder in the query to the value of messageContent
             */
            String query = "SELECT * FROM subscriber WHERE id = ? AND subscriber_password = ? ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(messageContent.get(0)));
            statement.setString(2, messageContent.get(5));
            /*
             * The result set is the result of the query
             * The while loop iterates over the result set and prints the values of the columns
             */
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                subscriberDetails.add(String.valueOf(rs.getInt("subscriber_id")));
                subscriberDetails.add(rs.getString("subscriber_fname"));
                subscriberDetails.add(rs.getString("subscriber_lname"));
                subscriberDetails.add(rs.getString("subscriber_phone_number"));
                subscriberDetails.add(rs.getString("subscriber_email"));
                subscriberDetails.add(rs.getString("subscriber_password"));
                subscriberDetails.add(String.valueOf(rs.getBoolean("subscriber_status")));
            }
            // Create the subscriber and return it
            return new Subscriber(Integer.parseInt(subscriberDetails.get(0)), subscriberDetails.get(1),
                    subscriberDetails.get(2), subscriberDetails.get(3), subscriberDetails.get(4), subscriberDetails.get(5), Boolean.parseBoolean(subscriberDetails.get(6)));
        } catch (SQLException e) {
            // If an error occur
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method edits the details of a specific subscriber
     *
     * @param messageContent The message content
     * @param conn           The connection to the database
     * @return The edited subscriber
     */
    default Subscriber editSubscriberDetails(Subscriber messageContent, Connection conn) {
        ArrayList<String> editedDetails = new ArrayList<>();
        try {
            /*
             * The query updates the first name, last name, phone number,
             * and email of the subscriber where the id matches the given value
             */
            String query = "UPDATE subscriber SET subscriber_fname = ?," +
                    " subscriber_lname = ?, subscriber_phone_number = ?, " +
                    "subscriber_email = ?, subscriber_password = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, messageContent.getFirstName());
            statement.setString(2, messageContent.getLastName());
            statement.setString(3, messageContent.getPhoneNumber());
            statement.setString(4, messageContent.getEmail());
            statement.setString(5, messageContent.getPassword());
            statement.setInt(5, messageContent.getID());
            /**
             *  Execute the update
             */
            statement.executeUpdate();
        } catch (SQLException e) {
            // If an error occur
            System.out.println("Error: With updating the subscriber in sql" + e);
            return null;
        }

        try {
            String query = "SELECT * FROM subscriber WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, messageContent.getID());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                editedDetails.add(String.valueOf(rs.getInt("subscriber_id")));
                editedDetails.add(rs.getString("subscriber_fname"));
                editedDetails.add(rs.getString("subscriber_lname"));
                editedDetails.add(rs.getString("subscriber_phone_number"));
                editedDetails.add(rs.getString("subscriber_email"));
                editedDetails.add(rs.getString("subscriber_password"));
                editedDetails.add(rs.getString("subscriber_status"));

                return new Subscriber(Integer.parseInt(editedDetails.get(0)), editedDetails.get(1),
                        editedDetails.get(2), editedDetails.get(3), editedDetails.get(4), editedDetails.get(5),  Boolean.parseBoolean(editedDetails.get(6)));
            }
        } catch (SQLException e) {
            // If an error occur
            System.out.println("Error: With exporting new subscriber details from sql" + e);
            return null;
        }
        return null;
    }
}



