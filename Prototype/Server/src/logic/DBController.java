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
    public static ArrayList<String> userLogin(ArrayList<String> messageContent, Connection conn) {
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

}
