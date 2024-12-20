package logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.*;

public class LibrarianController {

    private static LibrarianController instance = null;
    private LibrarianController() {
    }

    public static LibrarianController getInstance() {
        if (instance == null) {
            System.out.println("LibrarianController was created successfully");
            instance = new LibrarianController();
        }
        return instance;
    }

    /**
     * This method retrieves a list of all subscribers
     */
    public static ArrayList<Subscriber> getSubscribersList(Connection conn) {
        ArrayList<Subscriber> subscribersList = new ArrayList<>();
        boolean status;
        try {
            /*
             * The query selects all columns from the subscriber table
             * Execute the query
             */
            String query = "SELECT * FROM subscriber";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            /*
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
                subscriberDetails.add(rs.getString("subscriber_password"));
                subscriberDetails.add(String.valueOf(rs.getInt("subscriber_status")));

                if (subscriberDetails.get(6).equals("0")) {
                    status = false;
                } else {
                    status = true;
                }
                subscribersList.add(new Subscriber(Integer.parseInt(subscriberDetails.get(0)), subscriberDetails.get(1),
                            subscriberDetails.get(2), subscriberDetails.get(3), subscriberDetails.get(4), "XXX", status));
            }
            if (subscribersList.isEmpty()) {
                System.out.println("No subscribers found(getSubscribersList LibrarianController) ");
                return null;
            } else {
                System.out.println("Subscribers list found");
                return subscribersList;
            }

        } catch (SQLException e) {
            System.out.println("Error: With exporting subscribers from sql(getSubscribersList LibrarianController) " + e);
            return null;
        }
    }
}
