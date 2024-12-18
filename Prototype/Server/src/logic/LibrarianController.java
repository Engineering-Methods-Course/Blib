package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//! will be implemented in the future WIP
public interface LibrarianController {

    /**
     * This method retrieves a list of all subscribers
     */
    default ArrayList<ArrayList<String>> getSubscribersList(Connection conn) {
        ArrayList<ArrayList<String>> subscribersList = new ArrayList<>();
        try {
            /**
             * The query selects all columns from the subscriber table
             * Execute the query
             */
            String query = "SELECT * FROM subscriber";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            /**
             * If the query was successful, add the values of the columns to a list
             * Add the list to the subscribers list
             */
            while (rs.next()) {
                ArrayList<String> subscriberDetails = new ArrayList<>();
                subscriberDetails.add(String.valueOf(rs.getInt("subscriber_id")));
                subscriberDetails.add(rs.getString("subscriber_fname"));
                subscriberDetails.add(rs.getString("subscriber_lname"));
                subscriberDetails.add(rs.getString("subscriber_phone_number"));
                subscriberDetails.add(rs.getString("subscriber_email"));
                subscriberDetails.add(rs.getString("subscriber_status"));
                subscribersList.add(subscriberDetails);
            }
        } catch (SQLException e) {
            System.out.println("Error: With updating the subscriber in sql" + e);
            subscribersList.clear();
            return subscribersList; // sends an empty Arraylist if there is an error
        }
        return subscribersList;
    }
}
