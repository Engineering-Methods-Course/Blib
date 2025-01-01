package logic;

import common.Subscriber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBController {
    private static DBController instance = null;

    private DBController() {
    }

    /**
     * This method creates an instance of the DBController
     * if it does not exist
     *
     * @return The instance of the DBController
     */
    public static DBController getInstance() {
        if (instance == null) {
            System.out.println("DBController was created successfully");
            instance = new DBController();
        }
        return instance;
    }

    /**
     * case 100
     * This method logs in the user in to the system
     *
     * @param messageContent The message content
     * @param conn           The connection to the database
     * @return The user
     */
    public ArrayList<String> userLogin(ArrayList<String> messageContent, Connection conn) {
        try {
            ArrayList<String> userDetails = new ArrayList<>();
            int status;
            /*
             * The query selects all columns from the user table where the username matches a given value
             */
            String userQuery = "SELECT type, user_id FROM users WHERE username = ? AND password = ? ";
            PreparedStatement userStatement = conn.prepareStatement(userQuery);
            userStatement.setString(1, messageContent.get(0));
            userStatement.setString(2, messageContent.get(1));
            /*
             * The result set is the result of the query
             */
            ResultSet userRs = userStatement.executeQuery();
            if(userRs.next()) {
                String userId = userRs.getString("user_id");
                String type = userRs.getString("type");
                /*
                 * If the user is a subscriber, select the subscriber details
                 */
                if(type.equals("subscriber")){
                    String subQuery = "SELECT * FROM subscriber WHERE subscriber_id = ?";
                    PreparedStatement subStatement = conn.prepareStatement(subQuery);
                    subStatement.setString(1, userId);
                    ResultSet subRs = subStatement.executeQuery();
                    if(subRs.next()){
                        userDetails.add("subscriber");
                        userDetails.add(String.valueOf(subRs.getInt("subscriber_id")));
                        userDetails.add(subRs.getString("first_name"));
                        userDetails.add(subRs.getString("last_name"));
                        userDetails.add(subRs.getString("phone_number"));
                        userDetails.add(subRs.getString("email"));
                        status = subRs.getInt("status");
                        if(status == 0){
                            userDetails.add("false");
                        }
                        else{
                            userDetails.add("true");
                        }
                        userDetails.add(String.valueOf(subRs.getInt("detailed_subscription_history")));
                    }
                }
                /*
                 * If the user is a librarian, select the librarian details
                 */
                else{
                    String libQuery = "SELECT type, user_id FROM users WHERE username = ? AND password = ? ";
                    PreparedStatement libStatement = conn.prepareStatement(libQuery);
                    libStatement.setString(1, userId);
                    ResultSet subRs = libStatement.executeQuery();
                    if(subRs.next()){
                        userDetails.add("librarian");
                        userDetails.add(String.valueOf(subRs.getInt("librarian_id")));
                        userDetails.add(subRs.getString("first_name"));
                        userDetails.add(subRs.getString("last_name"));
                    }
                }
            }
            // No subscriber found
            if (!userDetails.isEmpty()) {
                System.out.println("User found");
                return userDetails;
            }
            System.out.println("No user found(Subscriber LogIn Failed)");
            return null;

        } catch (SQLException e) {
            // If an error occur
            System.out.println("Error: Login Failed" + e);
            return null;
        }
    }

    /**
     * case 306
     * This method gets the list of all subscribers in the system
     *
     * @param conn The connection to the database
     * @return The arraylist of all subscribers
     */
    public ArrayList<Subscriber> viewAllSubscribers(Connection conn){
        try{
            ArrayList<Subscriber> subscribersList = new ArrayList<>();
            int status;
            /*
             * The query selects all columns from the subscriber table
             */
            String getSubscribersQuery = "SELECT * FROM subscriber";
            PreparedStatement getSubscribersStatement = conn.prepareStatement(getSubscribersQuery);
            ResultSet getSubscribersRs = getSubscribersStatement.executeQuery();
            /*
             * If the query was successful, add the values of the columns to a list
             * Add the list to the subscribers list
             */
            while(getSubscribersRs.next()){
                ArrayList<String> subscriberDetails = new ArrayList<>();
                subscriberDetails.add(String.valueOf(getSubscribersRs.getInt("subscriber_id")));
                subscriberDetails.add(getSubscribersRs.getString("first_name"));
                subscriberDetails.add(getSubscribersRs.getString("last_name"));
                subscriberDetails.add(getSubscribersRs.getString("phone_number"));
                subscriberDetails.add(getSubscribersRs.getString("email"));
                status = getSubscribersRs.getInt("status");
                if(status == 0){
                    subscriberDetails.add("false");
                }
                else{
                    subscriberDetails.add("true");
                }
                subscriberDetails.add(String.valueOf(getSubscribersRs.getInt("detailed_subscription_history")));
                // Add the subscriber to the arraylist
                subscribersList.add(new Subscriber(Integer.parseInt(subscriberDetails.get(0)), subscriberDetails.get(1),
                        subscriberDetails.get(2), subscriberDetails.get(3), subscriberDetails.get(4),
                        Boolean.parseBoolean(subscriberDetails.get(5)), Integer.parseInt(subscriberDetails.get(6))));
            }
            // No subscriber found
            if(subscribersList.isEmpty()){
                System.out.println("No subscribers found");
                return null;
            }
            // Subscriber/s found
            else{
                System.out.println("Subscribers list found");
                return subscribersList;
            }
        // If an error occur
        }catch (SQLException e){
            System.out.println("Error: With exporting subscribers from sql(getSubscribersList LibrarianController) " + e);
            return null;
        }
    }

}
