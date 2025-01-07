package logic;

import java.sql.Blob;
import common.Book;
import common.Librarian;
import common.Subscriber;
import common.User;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static DBController getInstance(Connection connection) {
        if (instance == null) {
            System.out.println("DBController was created successfully");
            instance = new DBController();

        }
        return instance;
    }

    /**
     * This method converts a CSV file to a Blob
     *
     * @param csvFilePath The path to the CSV file
     * @return Blob
     * @throws IOException  If an error occurs
     * @throws SQLException If an error occurs
     */
    public static Blob convertCSVToBlob(String csvFilePath) throws IOException, SQLException {
        File csvFile = new File(csvFilePath);
        byte[] fileContent = new byte[(int) csvFile.length()];

        try (FileInputStream fis = new FileInputStream(csvFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
            bis.read(fileContent, 0, fileContent.length);
        }

        return new SerialBlob(fileContent);
    }
    /**
     * This method converts a Blob to a CSV file
     *
     * @param blob        The Blob
     * @param csvFilePath The path to the CSV file
     * @throws IOException  If an error occurs
     * @throws SQLException If an error occurs
     */
    public void convertBlobToCSV(Blob blob, String csvFilePath) throws IOException, SQLException {
        byte[] blobBytes = blob.getBytes(1, (int) blob.length());
        File csvFile = new File(csvFilePath);

        try (FileOutputStream fos = new FileOutputStream(csvFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(blobBytes);
        }
    }

    /**
     * This method writes data to a CSV file
     * @param csvFilePath The path to the CSV file
     */
    public void writeDataToCSV(String csvFilePath, String data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath, false))) {
            writer.println(data);
        }
    }
    /**
     * This method deletes a file
     *
     * @param filePath The path to the file
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    /**
     * case 100
     * This method logs in the user in to the system
     *
     * @param messageContent The message content
     * @param conn           The connection to the database
     * @return The user
     */
    public User userLogin(ArrayList<String> messageContent, Connection conn) {
        try {
            String username = messageContent.get(0);
            String password = messageContent.get(1);

            /*
             * The query selects all columns from the user table where the username matches a given value
             */

            String userQuery = "SELECT type, user_id FROM users WHERE username = ? AND password = ? ";
            PreparedStatement userStatement = conn.prepareStatement(userQuery);
            userStatement.setString(1, username);
            userStatement.setString(2, password);

            /*
             * The result set is the result of the query
             */

            ResultSet userRs = userStatement.executeQuery();
            if (userRs.next()) {
                int userId = userRs.getInt("user_id");
                String type = userRs.getString("type");

                /*
                 * If the user is a subscriber, select the subscriber details
                 */

                if (type.equals("subscriber")) {
                    String subQuery = "SELECT * FROM subscriber WHERE subscriber_id = ?";
                    PreparedStatement subStatement = conn.prepareStatement(subQuery);
                    subStatement.setInt(1, userId);
                    ResultSet subRs = subStatement.executeQuery();
                    if (subRs.next()) {
                        Subscriber subscriber = new Subscriber(subRs.getInt("subscriber_id"), subRs.getString("first_name"), subRs.getString("last_name"), subRs.getString("phone_number"), subRs.getString("email"), subRs.getInt("status") == 1, subRs.getInt("detailed_subscription_history"));
                        return subscriber;
                    }
                }

                /*
                 * If the user is a librarian, select the librarian details
                 */

                else {
                    String libQuery = "SELECT * FROM librarian WHERE librarian_id = ?";
                    PreparedStatement libStatement = conn.prepareStatement(libQuery);
                    libStatement.setInt(1, userId);
                    ResultSet subRs = libStatement.executeQuery();
                    if (subRs.next()) {
                        return new Librarian(subRs.getInt("librarian_id"), subRs.getString("first_name"), subRs.getString("last_name"));
                    }
                }
            }
            // No subscriber found
            System.out.println("No user found (userLogin)");
            return null;
        } catch (SQLException e) {
            // If an error occur
            System.out.println("Error: Login Failed (userLogin) " + e);
            return null;
        }
    }

    /**
     * case 200
     * This method searches for books by a partial name match
     *
     * @param bookName The partial name of the book
     * @param conn     The connection to the database
     * @return The list of books that match the partial name
     */
    public ArrayList<Book> searchBookByName(String bookName, Connection conn) {
        try {
            ArrayList<Book> books = new ArrayList<>();

            /*
             * The query selects all columns from the book table where the name matches a given value
             */

            String findBookQuery = "SELECT * FROM book WHERE name LIKE ?";
            PreparedStatement findBookStatement = conn.prepareStatement(findBookQuery);
            findBookStatement.setString(1, "%" + bookName + "%");

            ResultSet rs = findBookStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"), rs.getInt("lost_copies"));
                books.add(book);
            }
            if (books.isEmpty()) {
                System.out.println("No books found (searchBookByName)");
                return null;
            }
            System.out.println("Books found (searchBookByName)");
            return books;
        } catch (SQLException e) {
            System.out.println("Error: With exporting books from sql (searchBookByName) " + e);
            return null;
        }
    }

    /**
     * case 202
     * This method searches for books by a genre match
     *
     * @param bookGenre The partial genre of the book
     * @param conn      The connection to the database
     * @return The list of books that match the partial genre
     */
    public ArrayList<Book> searchBookByGenre(String bookGenre, Connection conn) {
        try {
            ArrayList<Book> books = new ArrayList<>();

            /*
             * The query selects all columns from the book table where the genre matches a given value
             */

            String findBookQuery = "SELECT * FROM book WHERE main_genre = ?";
            PreparedStatement findbookStatement = conn.prepareStatement(findBookQuery);
            findbookStatement.setString(1, bookGenre);

            ResultSet rs = findbookStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"), rs.getInt("lost_copies"));
                books.add(book);
            }
            if (books.isEmpty()) {
                System.out.println("No books found (searchBookByName)");
                return null;
            }
            System.out.println("Books found (searchBookByGenre)");
            return books;
        } catch (SQLException e) {
            System.out.println("Error: With exporting books from sql (searchBookByGenre) " + e);
            return null;
        }
    }

    /**
     * case 204
     * This method gets the list of all books in the system by description
     *
     * @param conn The connection to the database
     * @return The arraylist of all books
     */
    public ArrayList<Book> searchBookByDescription(String text, Connection conn) {
        try {
            ArrayList<Book> books = new ArrayList<>();

            /*
             * The query selects all columns from the book table where the description matches a given value
             */

            String findBookQuery = "SELECT * FROM book WHERE MATCH(description) AGAINST(? IN NATURAL LANGUAGE MODE)";
            PreparedStatement findbookStatement = conn.prepareStatement(findBookQuery);
            findbookStatement.setString(1, text);

            ResultSet rs = findbookStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"), rs.getInt("lost_copies"));
                books.add(book);
            }
            if (books.isEmpty()) {
                System.out.println("No books found (searchBookByName)");
                return null;
            }
            System.out.println("Books found (searchBookByDescription)");
            return books;
        } catch (SQLException e) {
            System.out.println("Error: With exporting books from sql (searchBookByDescription) " + e);
            return null;
        }
    }

    /**
     * case 212
     * This method extends the return date of a borrowed book
     *
     * @param messageContent Array list containing the subscriber ID and the book ID and the new return date
     * @param conn           The connection to the database
     * @return Array list containing true if the book was reserved successfully and false if not with the error message
     */
    public ArrayList<String> extendBookBorrowTimeSubscriber(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        int subscriberId = Integer.parseInt(messageContent.get(0));
        int copyId = Integer.parseInt(messageContent.get(1));
        Timestamp newReturnDate = null;
        try {

            /*
             * Checks if the subscriber is frozen
             */

            String checkSubscriberQuery = "SELECT status FROM subscriber WHERE subscriber_id = ?";
            PreparedStatement checkSubscriberStatement = conn.prepareStatement(checkSubscriberQuery);
            checkSubscriberStatement.setInt(1, subscriberId);
            ResultSet checkSubscriberRs = checkSubscriberStatement.executeQuery();
            if (checkSubscriberRs.next()) {
                if (checkSubscriberRs.getInt("status") == 0) {
                    response.add("false");
                    response.add("Subscriber is frozen");
                    return response;
                }
            }

            /*
             * Checks if less than a week is left for the book to be returned (book can be extended only if less than a week is left)
             */

            String checkReturnDateQuery = "SELECT DATEDIFF(expected_return_date, NOW()) AS days_diff , expected_return_date FROM borrow WHERE subscriber_id = ? AND copy_id = ?";
            PreparedStatement checkReturnDateStatement = conn.prepareStatement(checkReturnDateQuery);
            checkReturnDateStatement.setInt(1, subscriberId);
            checkReturnDateStatement.setInt(2, copyId);
            ResultSet checkReturnDateRs = checkReturnDateStatement.executeQuery();
            if (checkReturnDateRs.next()) {
                LocalDateTime localDateTime = checkReturnDateRs.getTimestamp("expected_return_date").toLocalDateTime().plusDays(7);
                newReturnDate = Timestamp.valueOf(localDateTime);
                int daysDiff = checkReturnDateRs.getInt("days_diff");
                if (daysDiff > 7) {
                    response.add("false");
                    response.add("Cannot extend the return date due to having more than a week left");
                    return response;
                }
            }

            /*
             * Checks if the book is reserved (ordered)
             */

            String checkReservedQuery = "SELECT reserved_copies  FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement checkReservedStatement = conn.prepareStatement(checkReservedQuery);
            checkReservedStatement.setInt(1, copyId);
            ResultSet checkReservedRs = checkReservedStatement.executeQuery();
            if (checkReservedRs.next()) {
                if (checkReservedRs.getInt("reserved_copies") != 0) {
                    response.add("false");
                    response.add("the book had been reserved");
                    return response;
                }
            }

            /*
             * The query updates the expected return date of the borrow table where the subscriber ID and the book ID matches the given values
             */
            String extendQuery = "UPDATE borrow SET expected_return_date = ? WHERE subscriber_id = ? AND copy_id = ?";
            PreparedStatement extendStatement = conn.prepareStatement(extendQuery);
            extendStatement.setTimestamp(1, newReturnDate);
            extendStatement.setInt(2, Integer.parseInt(messageContent.get(0)));
            extendStatement.setInt(3, Integer.parseInt(messageContent.get(1)));
            extendStatement.executeUpdate();

            response.add("true");
            return response;
        } catch (SQLException e) {
            // If an error occur
            response.add("false");
            response.add("Problem with extending the return date");
            System.out.println("Error: With extending the return date (extendReturnDate) " + e);
            return response;
        }
        //! need to add to log
    }

    /**
     * case 216
     * This method edits the subscriber details
     *
     * @param messageContent the new subscriber details
     * @param conn           The connection to the database
     * @return array list containing true if the subscriber was edited successfully and false if not with the error message
     */
    public ArrayList<String> editSubscriberDetails(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = Integer.parseInt(messageContent.get(0));
            String subscriberEmail = messageContent.get(1);
            String subscriberPhoneNumber = messageContent.get(2);
            String subscriberFirstName = messageContent.get(3);
            String subscriberLastName = messageContent.get(4);

            /*
             * The query updates the phone number and email of the subscriber where the id matches the given value
             */

            String subscriberInfoQuery = "UPDATE subscriber SET phone_number = ?, email = ?, first_name = ?, last_name = ?  WHERE subscriber_id = ?";
            PreparedStatement subscriberInfoStatement = conn.prepareStatement(subscriberInfoQuery);
            subscriberInfoStatement.setString(1, subscriberPhoneNumber);
            subscriberInfoStatement.setString(2, subscriberEmail);
            subscriberInfoStatement.setString(3, subscriberFirstName);
            subscriberInfoStatement.setString(4, subscriberLastName);
            subscriberInfoStatement.setInt(5, subscriberId);
            subscriberInfoStatement.executeUpdate();
            response.add("true");
            response.add(subscriberPhoneNumber);
            response.add(subscriberEmail);
            response.add(subscriberFirstName);
            response.add(subscriberLastName);
            return response;
        } catch (SQLException e) {
            // If an error occur
            response.add("false");
            response.add("Problem with updating the subscriber details");
            System.out.println("Error: With updating the subscriber (editSubscriberDetails) " + e);
            return response;
        }
        //! need to add to log
    }

    /**
     * case 218
     * This method edits the subscriber login details
     *
     * @param messageContent the new subscriber details
     * @param conn           The connection to the database
     * @return array list containing true if the subscriber was edited successfully and false if not with the error message
     */
    public ArrayList<String> editSubscriberPassword(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = Integer.parseInt(messageContent.get(0));
            String password = messageContent.get(1);

            /*
             * The query updates the username and password of the subscriber where the id matches the given value
             */

            String subscriberUserinfoQuery = "UPDATE users SET password = ? WHERE user_id = ?";
            PreparedStatement subscriberUserinfoStatement = conn.prepareStatement(subscriberUserinfoQuery);
            subscriberUserinfoStatement.setString(1, password);
            subscriberUserinfoStatement.setInt(2, subscriberId);
            subscriberUserinfoStatement.executeUpdate();
            response.add("true");
            return response;
        } catch (SQLException e) {
            // If an error occur
            response.add("false");
            response.add("Problem with updating the subscriber password");
            System.out.println("Error: With updating the subscriber (editSubscriberDetails) " + e);
            return response;
        }
        //! need to add to log
    }

    /**
     * case 300
     * This method registers a new subscriber in the system
     *
     * @param messageContent the new subscriber details
     * @param conn           The connection to the database
     * @return array list containing true if the subscriber was added successfully and false if not with the error message
     */
    public ArrayList<String> registerNewSubscriber(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = 0;
            int historyId = 0;

            String csvFile = String.format("%s_History.csv", messageContent.get(0));

            /*
             * Check if the username already exists
             */

            String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkUserStatement = conn.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1, messageContent.get(0));
            ResultSet checkUserRs = checkUserStatement.executeQuery();
            if (checkUserRs.next() && checkUserRs.getInt(1) > 0) {
                response.add("false");
                response.add("Username already exists");
                return response;
            }

            /*
             * Create a CSV history file for the subscriber
             */

            writeDataToCSV(csvFile, String.format("%s,registered,account was created", LocalDateTime.now()));

            /*
             * Turn the CSV file into a Blob
             */

            Blob csvBlob = convertCSVToBlob(csvFile);
            conn.setAutoCommit(false);

            /*
             * create a new history record for the new subscriber
             */

            String historyQuery = "INSERT INTO subscription_history  (details) VALUES (?)"; // Adjust columns as needed
            PreparedStatement historyStatement = conn.prepareStatement(historyQuery, Statement.RETURN_GENERATED_KEYS);
            historyStatement.setBlob(1, csvBlob);
            historyStatement.executeUpdate();
            ResultSet generatedKeysHistoryId = historyStatement.getGeneratedKeys();
            if (generatedKeysHistoryId.next()) {
                historyId = generatedKeysHistoryId.getInt(1);
            } else {
                throw new SQLException("Error: Getting new history ID");
            }

            /*
             * create a new user record for the new subscriber
             */

            String userQuery = "INSERT INTO users (username, password, type) VALUES (?, ?, ?)";
            PreparedStatement userStatement = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, messageContent.get(0));
            userStatement.setString(2, "Aa123456");
            userStatement.setString(3, "subscriber");
            userStatement.executeUpdate();
            ResultSet generatedKeysUserId = userStatement.getGeneratedKeys();
            if (generatedKeysUserId.next()) {
                subscriberId = generatedKeysUserId.getInt(1);
            } else {
                throw new SQLException("Error: Getting new user ID");
            }

            /*
             * create a new subscriber record for the new subscriber
             */

            String subscriberQuery = "INSERT INTO subscriber (subscriber_id, first_name, last_name, phone_number, email, detailed_subscription_history) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement subscriberStatement = conn.prepareStatement(subscriberQuery);
            subscriberStatement.setInt(1, subscriberId);
            subscriberStatement.setString(2, messageContent.get(1));
            subscriberStatement.setString(3, messageContent.get(2));
            subscriberStatement.setString(4, messageContent.get(3));
            subscriberStatement.setString(5, messageContent.get(4));
            subscriberStatement.setInt(6, historyId);
            subscriberStatement.executeUpdate();

            // Delete the CSV file
            deleteFile(csvFile);

            /*
                * Commit the transaction
             */

            conn.commit();
            response.add("true");
            String registerInfo = String.format("%s %s was registered successfully\n ID: %s\n Username: %s temporary password: %s", messageContent.get(1), messageContent.get(2), subscriberId, messageContent.get(0), "Aa123456");
            response.add(registerInfo);
        } catch (SQLException e) {
            // If an error occur rollback the transaction
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            System.out.println("Error: Registering new subscriber" + e);
            response.add("false");
            response.add("Problem with registering the subscriber");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // Set auto-commit back to true
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }

        return response;
    }

    /**
     * case 302
     * This method borrows a book to a subscriber
     *
     * @param messageContent Array list containing the subscriber ID and the book ID and return date
     * @param conn           The connection to the database
     * @return Array list containing true if the book was borrowed successfully and false if not with the error message
     */
    public ArrayList<String> borrowBookToSubscriber(ArrayList<String> messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = Integer.parseInt(messageContent.get(0));
            int copyId = Integer.parseInt(messageContent.get(1));
            // get the return date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(messageContent.get(2), formatter);
            Timestamp returnDate = Timestamp.valueOf(localDateTime);

            /*
             * This query selects the status of the book where the book ID matches the given value
             * and checks if subscriber is not frozen
             */

            String checkSubscriberQuery = "SELECT status FROM subscriber WHERE subscriber_id = ?";
            PreparedStatement checkSubscriberStatement = conn.prepareStatement(checkSubscriberQuery);
            checkSubscriberStatement.setInt(1, subscriberId);
            ResultSet checkSubscriberRs = checkSubscriberStatement.executeQuery();
            if (checkSubscriberRs.next()) {
                if (checkSubscriberRs.getInt("status") == 1) {
                    response.add("false");
                    response.add("Subscriber is frozen");
                    return response;
                }
            }

            /*
             * This query checks whether the book is available
             */

            String checkBookQuery = "SELECT available FROM book_copy WHERE copy_id = ?";
            PreparedStatement checkBookStatement = conn.prepareStatement(checkBookQuery);
            checkBookStatement.setInt(1, copyId);
            ResultSet checkBookRs = checkBookStatement.executeQuery();
            if (checkBookRs.next()) {
                if (checkBookRs.getInt("available") == 0) {
                    response.add("false");
                    response.add("Book is not available at the moment in the system need to preform return first");
                    return response;
                }
            }

            /*
             * check if the subscriber had reserved the book or if its reserved by someone else
             */

            boolean wasReserved = false;
            String bookReservedByTheSubscriberQuery = "SELECT subscriber_id FROM reservation WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?) ORDER BY reservation_date ASC LIMIT 1";
            PreparedStatement bookReservedByTheSubscriberStatement = conn.prepareStatement(bookReservedByTheSubscriberQuery);
            bookReservedByTheSubscriberStatement.setInt(1, copyId);
            ResultSet bookReservedByTheSubscriberRs = bookReservedByTheSubscriberStatement.executeQuery();
            if (bookReservedByTheSubscriberRs.next()) {
                int firstSubscriberId = bookReservedByTheSubscriberRs.getInt("subscriber_id");
                if (firstSubscriberId == subscriberId) {
                    // The subscriber was the first one to reserve the book
                    String deleteReservationQuery = "DELETE FROM reservation WHERE subscriber_id = ? AND serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                    PreparedStatement deleteReservationStatement = conn.prepareStatement(deleteReservationQuery);
                    deleteReservationStatement.setInt(1, subscriberId);
                    deleteReservationStatement.setInt(2, copyId);
                    deleteReservationStatement.executeUpdate();
                    wasReserved = true;
                }
            }
            // check if the book is reserved by someone else
            if (!wasReserved) {
                String checkReservedQuery = "SELECT copies, reserved_copies, borrowed_copies  FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                PreparedStatement checkReservedStatement = conn.prepareStatement(checkReservedQuery);
                checkReservedStatement.setInt(1, copyId);
                ResultSet checkReservedRs = checkReservedStatement.executeQuery();
                if (checkReservedRs.next()) {
                    int copies = checkReservedRs.getInt("copies");
                    int reservedCopies = checkReservedRs.getInt("reserved_copies");
                    int borrowedCopies = checkReservedRs.getInt("borrowed_copies");
                    if (copies <= (reservedCopies + borrowedCopies)) {
                        response.add("false");
                        response.add("Book is reserved");
                        return response;
                    }
                }
            }

            conn.setAutoCommit(false);


            /*
             * The query updates the status of the book_copy to unavailable (0)
             */

            String updateCopyQuery = "UPDATE book_copy SET available = ? WHERE copy_id = ?";
            PreparedStatement updateCopyStatement = conn.prepareStatement(updateCopyQuery);
            updateCopyStatement.setInt(1, 0);
            updateCopyStatement.executeUpdate();

            /*
             * The query update the amount of borrowed books in the book table
             */

            String updateBookQuery = "UPDATE book SET borrowed_copies = borrowed_copies + 1" + "WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement updateBookStatement = conn.prepareStatement(updateBookQuery);
            updateBookStatement.setInt(1, copyId); // copy_id
            updateBookStatement.executeUpdate();

            /*
             * The query inserts the book details into the borrow table
             */

            String borrowQuery = "INSERT INTO borrow (subscriber_id, copy_id, expected_return_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement borrowStatement = conn.prepareStatement(borrowQuery);
            borrowStatement.setInt(1, copyId);
            borrowStatement.setInt(2, subscriberId);
            borrowStatement.setTimestamp(3, returnDate);
            borrowStatement.setString(4, "borrowed");
            borrowStatement.executeUpdate();


            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            System.out.println("Error: Borrowing book" + e);
            response.add("false");
            response.add("Problem with borrowing the book");

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
        //! need to add to log
    }

    /**
     * case 304
     * This method returns a book from a subscriber
     *
     * @param messageContent Array list containing the subscriber ID and the book ID
     * @param conn           The connection to the database
     * @return Array list containing true if the book was returned successfully and false if not with the error message
     */
    public ArrayList<String> returnBookFromSubscriber(String messageContent, Connection conn) {
        ArrayList<String> response = new ArrayList<>();
        int subscriberId = 0;
        int copyId = Integer.parseInt(messageContent);

        try {

            /*
             * Created a date object to get the current date
             */

            conn.setAutoCommit(false);

            /*
             * The query updates the status of the book_copy to available (1)
             */

            String updateCopyQuery = "UPDATE book_copy SET available = ? WHERE copy_id = ?";
            PreparedStatement updateCopyStatement = conn.prepareStatement(updateCopyQuery);
            updateCopyStatement.setInt(1, 1);
            updateCopyStatement.setInt(2, copyId);
            updateCopyStatement.executeUpdate();

            /*
             * The query updates the amount of borrowed books in the book table
             */

            String updateBookQuery = "UPDATE book SET borrowed_copies = borrowed_copies - 1" + "WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement updateBookStatement = conn.prepareStatement(updateBookQuery);
            updateBookStatement.setInt(1, copyId);
            updateBookStatement.executeUpdate();

            /*
             * The query selects from borrow table subscriber_id where copy_id matches the given value
             * and the status is borrowed
             */
            String getSubscriberQuery = "SELECT subscriber_id FROM borrow WHERE copy_id = ? AND status = 'borrowed'";
            PreparedStatement getSubscriberStatement = conn.prepareStatement(getSubscriberQuery);
            getSubscriberStatement.setInt(1, Integer.parseInt(messageContent));
            ResultSet getSubscriberRs = getSubscriberStatement.executeQuery();
            if (getSubscriberRs.next()) {
                subscriberId = getSubscriberRs.getInt("subscriber_id");
            }

            /*
             * The query updates the status of the borrow table to returned and the return date to the current date
             */
            String returnQuery = "UPDATE borrow SET status = ?, return_date = ? WHERE subscriber_id = ? AND copy_id = ?";
            PreparedStatement returnStatement = conn.prepareStatement(returnQuery);
            returnStatement.setString(1, "returned");
            returnStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            returnStatement.setInt(3, subscriberId);
            returnStatement.setInt(4, copyId);
            returnStatement.executeUpdate();

            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            System.out.println("Error: Returning book" + e);
            response.add("false");
            response.add("Problem with returning the book");

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        //! notify reservation if needed
        //! need to add to log
        return response;
    }

    /**
     * case 306
     * This method gets the list of all subscribers in the system
     *
     * @param conn The connection to the database
     * @return The arraylist of all subscribers
     */
    public ArrayList<Subscriber> viewAllSubscribers(Connection conn) {
        try {
            ArrayList<Subscriber> subscribersList = new ArrayList<>();
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
            while (getSubscribersRs.next()) {
                Subscriber subscriber = new Subscriber(getSubscribersRs.getInt("subscriber_id"), getSubscribersRs.getString("first_name"), getSubscribersRs.getString("last_name"), getSubscribersRs.getString("phone_number"), getSubscribersRs.getString("email"), getSubscribersRs.getInt("status") == 1, getSubscribersRs.getInt("detailed_subscription_history"));
                subscribersList.add(subscriber);
            }
            // Subscriber/s found
            if (!subscribersList.isEmpty()) {
                System.out.println("Subscribers list found (viewAllSubscribers)");
                return subscribersList;

            }
            // No subscriber found
            else {
                System.out.println("No subscribers found (viewAllSubscribers)");
                return null;
            }
            // If an error occur
        } catch (SQLException e) {
            System.out.println("Error: With exporting subscribers from sql(viewAllSubscribers) " + e);
            return null;
        }
    }

    /**
     * case 308
     * This method returns a specific subscriber's details to the librarian by the subscriber ID
     *
     * @param subscriberId The subscriber ID
     * @param conn         The connection to the database
     * @return The subscriber
     */
    //! ask or about if needed
    public Subscriber viewSubscriberDetails(int subscriberId, Connection conn) {
        try {

            /*
             * The query selects all columns from the subscriber table where the subscriber ID matches the given value
             */

            String getSubscriberQuery = "SELECT * FROM subscriber WHERE subscriber_id = ?";
            PreparedStatement getSubscriberStatement = conn.prepareStatement(getSubscriberQuery);
            getSubscriberStatement.setInt(1, subscriberId);
            ResultSet getSubscriberRs = getSubscriberStatement.executeQuery();
            /*
             * If the query was successful, add the values of the columns to a list
             */

            if (getSubscriberRs.next()) {
                Subscriber subscriber = new Subscriber(getSubscriberRs.getInt("subscriber_id"), getSubscriberRs.getString("first_name"), getSubscriberRs.getString("last_name"), getSubscriberRs.getString("phone_number"), getSubscriberRs.getString("email"), getSubscriberRs.getInt("status") == 1, getSubscriberRs.getInt("detailed_subscription_history"));
                System.out.println("Subscriber found (viewSubscriberDetails)");
                return subscriber;
            }
            // No subscriber found
            else {
                System.out.println("No subscriber found (viewSubscriberDetails)");
                return null;
            }
            // If an error occur
        } catch (SQLException e) {
            System.out.println("Error: With exporting subscriber from sql(viewSubscriberDetails) " + e);
            return null;
        }
    }

}
