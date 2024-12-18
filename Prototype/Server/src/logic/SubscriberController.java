package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SubscriberController {
    /**
     * This method logins the subscriber to the system
     * and displays the details of a specific subscriber
     * @param messageContent
     */
    default ArrayList<String> subscriberLogin(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> subscriberDetails = new ArrayList<>();
        try {
            /**
             * The query selects all columns from the subscriber table where the id matches a given value
             * stmt.setString(1, messageContent) sets the first placeholder in the query to the value of messageContent
             */
            String query = "SELECT * FROM subscriber WHERE id = ? AND subscriber_password = ? ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(messageContent.get(0)));
            statement.setString(2, messageContent.get(5));
            /**
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
                subscriberDetails.add(rs.getString("subscriber_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // If the subscriber details are empty, add an error message
        if(subscriberDetails.isEmpty())
        {
            subscriberDetails.add("Invalid ID or password");
        }
        return subscriberDetails;
    }

    /**
     * This method edits the details of a specific subscriber
     * @param messageContent
     */
    default ArrayList<String> editSubscriberDetails(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> editedDetails = new ArrayList<>();
        try {
            /**
             * The query updates the first name, last name, phone number,
             * and email of the subscriber where the id matches the given value
             */
            String query = "UPDATE subscriber SET subscriber_fname = ?," +
                    " subscriber_lname = ?, subscriber_phone_number = ?, " +
                    "subscriber_email = ?, subscriber_password = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, messageContent.get(1));
            statement.setString(2, messageContent.get(2));
            statement.setString(3, messageContent.get(3));
            statement.setString(4, messageContent.get(4));
            statement.setString(5, messageContent.get(5));
            statement.setInt(5, Integer.parseInt(messageContent.get(0)));
            /**
             *  Execute the update
             */
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error: With updating the subscriber in sql" + e);
            return editedDetails; // sends an empty Arraylist if there is an error
        }

        try
        {
            String query = "SELECT * FROM subscriber WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(messageContent.get(0)));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                editedDetails.add(String.valueOf(rs.getInt("subscriber_id")));
                editedDetails.add(rs.getString("subscriber_fname"));
                editedDetails.add(rs.getString("subscriber_lname"));
                editedDetails.add(rs.getString("subscriber_phone_number"));
                editedDetails.add(rs.getString("subscriber_email"));
                editedDetails.add(rs.getString("subscriber_password"));
                editedDetails.add(rs.getString("subscriber_status"));
            }
        } catch (SQLException e)
        {
            System.out.println("Error: With exporting new subscriber details from sql" + e);
            editedDetails.clear();
            return editedDetails; // sends an empty Arraylist if there is an error
        }

        return editedDetails;
    }
}



