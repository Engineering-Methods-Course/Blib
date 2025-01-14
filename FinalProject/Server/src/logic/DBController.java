package logic;

import common.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static volatile DBController instance;
    Connection conn = null;
    private final NotificationController notificationController;

    private DBController() {
        notificationController = NotificationController.getInstance();
        connectToDb();
    }

    /**
     * This method creates an instance of the DBController
     * if it does not exist
     *
     * @return The instance of the DBController
     */
    public static DBController getInstance() {
        if (instance == null) {
            synchronized (DBController.class) {
                if (instance == null) {
                    System.out.println("DBController was created successfully");
                    instance = new DBController();
                }
            }
        }
        return instance;
    }

    /**
     * Connect to the database and create a connection
     */
    private void connectToDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            /* handle the error*/
            System.out.println("Driver definition failed");
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "Aa123456");
            System.out.println("SQL connection succeed");

        } catch (SQLException ex) {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    /**
     * case 100
     * This method logs in the user in to the system
     *
     * @param messageContent The message content
     * @return The user
     */
    public User userLogin(ArrayList<String> messageContent) {
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
                        return new Subscriber(subRs.getInt("subscriber_id"), subRs.getString("first_name"), subRs.getString("last_name"), subRs.getString("phone_number"), subRs.getString("email"), subRs.getInt("status") == 1, subRs.getInt("detailed_subscription_history"));
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
     * @return The list of books that match the partial name
     */
    public ArrayList<Book> searchBookByName(String bookName) {
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
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"));
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
     * @return The list of books that match the partial genre
     */
    public ArrayList<Book> searchBookByGenre(String bookGenre) {
        try {
            ArrayList<Book> books = new ArrayList<>();

            /*
             * The query selects all columns from the book table where the genre matches a given value
             */

            String findBookQuery = "SELECT * FROM book WHERE main_genre = ?";
            PreparedStatement findBookStatement = conn.prepareStatement(findBookQuery);
            findBookStatement.setString(1, bookGenre);

            ResultSet rs = findBookStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"));
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
     * @return The arraylist of all books
     */
    public ArrayList<Book> searchBookByDescription(String text) {
        try {
            ArrayList<Book> books = new ArrayList<>();

            /*
             * The query selects all columns from the book table where the description matches a given value
             */

            String findBookQuery = "SELECT * FROM book WHERE MATCH(description) AGAINST(? IN NATURAL LANGUAGE MODE)";
            PreparedStatement findBookStatement = conn.prepareStatement(findBookQuery);
            findBookStatement.setString(1, text);

            ResultSet rs = findBookStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                Book book = new Book(rs.getInt("serial_number"), rs.getString("name"), rs.getString("main_genre"), rs.getString("description"), rs.getInt("copies"), rs.getInt("reserved_copies"), rs.getInt("borrowed_copies"));
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
     * case 206
     * This method checks if a book is available
     *
     * @param serialNumber The serial number of the book
     * @return [true, book location] if the book is available
     * [false, nearest available date] if the book is not available
     */
    public ArrayList<String> checkBookAvailability(int serialNumber) {
        try {
            ArrayList<String> response = new ArrayList<>();
            ArrayList<Integer> unAvailableCopies = new ArrayList<>();

            /*
             * The query select all book copies and check if there is book copy available
             */

            String checkAvailabilityQuery = "SELECT available, copy_id, shelf_location FROM book_copy WHERE serial_number = ?";
            PreparedStatement checkAvailabilityStatement = conn.prepareStatement(checkAvailabilityQuery);
            checkAvailabilityStatement.setInt(1, serialNumber);
            ResultSet checkAvailabilityRs = checkAvailabilityStatement.executeQuery();
            while (checkAvailabilityRs.next()) {
                boolean available = checkAvailabilityRs.getInt("available") == 1;

                /*
                 * If there is a copy available return true and the location of the book
                 */

                if (available) {
                    response.add("true");
                    response.add(checkAvailabilityRs.getString("shelf_location"));
                    return response;
                }

                /*
                 * If there is no copy available add the copy id to the list
                 */

                else {
                    unAvailableCopies.add(checkAvailabilityRs.getInt("copy_id"));
                }
            }

            /*
             * If there are no copies available, get the nearest return date
             */

            if (!unAvailableCopies.isEmpty()) {

                /*
                 * Create a query string with the number of copies dynamically
                 */

                StringBuilder queryBuilder = new StringBuilder("SELECT MIN(expected_return_date) AS nearest_return_date FROM borrow WHERE copy_id IN (");
                for (int i = 0; i < unAvailableCopies.size(); i++) {
                    queryBuilder.append("?");
                    if (i < unAvailableCopies.size() - 1) {
                        queryBuilder.append(",");
                    }
                }
                queryBuilder.append(")");

                /*
                 * Add the parameters to the prepared statement
                 */

                PreparedStatement nearestReturnDateStatement = conn.prepareStatement(queryBuilder.toString());
                for (int i = 0; i < unAvailableCopies.size(); i++) {
                    nearestReturnDateStatement.setInt(i + 1, unAvailableCopies.get(i));
                }
                ResultSet nearestReturnDateRs = nearestReturnDateStatement.executeQuery();

                /*
                 * If the query was successful, add the values of the book to a list
                 */

                if (nearestReturnDateRs.next()) {
                    Date nearestReturnDate = nearestReturnDateRs.getDate("nearest_return_date");
                    if (nearestReturnDate != null) {
                        response.add("false");
                        response.add(nearestReturnDate.toString());
                    } else {
                        response.add("false");
                        response.add("No return dates found for the provided book IDs");
                    }
                }
            }
            return response;

        } catch (Exception e) {
            System.out.println("Error: With checking the book availability (checkBookAvailability) " + e);
            return null;
        }
    }

    /**
     * case 208
     * This method reserves (orders) a book for a subscriber
     *
     * @param messageContent Array list containing the subscriber ID and the book Serial number
     * @return Array list containing true if the book was reserved successfully and false if not with the error message
     */
    public ArrayList<String> reserveBook(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = Integer.parseInt(messageContent.get(0));
            int serialNumber = Integer.parseInt(messageContent.get(1));
            String bookName = "";

            conn.setAutoCommit(false);

            /*
             * Check if the subscriber is frozen
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
            else {
                response.add("false");
                response.add("Subscriber not found");
                return response;
            }

            /*
             * This query select the total copies of the book, the borrowed copies and the reserved copies
             */

            String totalCopiesQuery = "SELECT copies, borrowed_copies, reserved_copies, name FROM book WHERE serial_number = ?";
            PreparedStatement totalCopiesStatement = conn.prepareStatement(totalCopiesQuery);
            totalCopiesStatement.setInt(1, serialNumber);
            ResultSet totalCopiesRs = totalCopiesStatement.executeQuery();
            if (totalCopiesRs.next()) {
                int totalCopies = totalCopiesRs.getInt("copies");
                int borrowedCopies = totalCopiesRs.getInt("borrowed_copies");
                int reservedCopies = totalCopiesRs.getInt("reserved_copies");
                bookName = totalCopiesRs.getString("name");

                /*
                 * Check if all copies are borrowed
                 */

                if (totalCopies != borrowedCopies) {
                    response.add("false");
                    response.add("Can't reserve the book, not all copies are borrowed");
                    return response;
                }

                /*
                 * Check if the book is fully reserved
                 */

                if (reservedCopies == totalCopies) {
                    response.add("false");
                    response.add("Can't reserve the book, the book is fully reserved");
                    return response;
                }
            }
            else {
                response.add("false");
                response.add("Book not found");
                return response;
            }

            /*
             * Reserve the book
             */

            String reserveQuery = "INSERT INTO reservation (subscriber_id, serial_number, reserve_date) VALUES (?, ?, ?)";
            PreparedStatement reserveStatement = conn.prepareStatement(reserveQuery);
            reserveStatement.setInt(1, subscriberId);
            reserveStatement.setInt(2, serialNumber);
            reserveStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            reserveStatement.executeUpdate();

            /*
             * Update the reserved copies of the book
             */

            String updateReservedQuery = "UPDATE book SET reserved_copies = reserved_copies + 1 WHERE serial_number = ?";
            PreparedStatement updateReservedStatement = conn.prepareStatement(updateReservedQuery);
            updateReservedStatement.setInt(1, serialNumber);
            updateReservedStatement.executeUpdate();

            /*
             * Update subscriber history
             */

            updateHistory(subscriberId, "reserve", "Book: " + bookName + " of serial number: " + serialNumber + " has been reserved");

            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");
        } catch (SQLException e) {

            /*
             * If an error occur rollback the transaction
             */

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            response.add("false");
            response.add("Problem with reserving the book");
            System.out.println("Error: With reserving the book (reserveBookToSubscriber) " + e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 210
     * this method show subscriber borrowed list
     *
     * @param subscriberId The ID of the subscriber
     * @return The list of borrowed books
     */
    public ArrayList<BorrowedBook> showSubscriberBorrowedBooks(int subscriberId) {
        try {
            ArrayList<BorrowedBook> borrowedBooks = new ArrayList<>();

            /*
             * The query selects all columns from the borrow table where the subscriber ID matches a given value
             */

            String findBorrowQuery = "SELECT * FROM borrow WHERE subscriber_id = ? AND status = 'borrowed'";
            PreparedStatement findBorrowStatement = conn.prepareStatement(findBorrowQuery);
            findBorrowStatement.setInt(1, subscriberId);

            ResultSet rs = findBorrowStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */
            while (rs.next()) {

                /*
                 * This query selects the book name of the subscriber
                 */
                String getBookNameQuery = "SELECT name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
                getBookNameStatement.setInt(1, rs.getInt("copy_id"));
                ResultSet getBookNameRs = getBookNameStatement.executeQuery();
                String bookName = "";
                if (getBookNameRs.next()) {
                    bookName = getBookNameRs.getString("name");
                }
                BorrowedBook borrow = new BorrowedBook(rs.getInt("copy_id"), rs.getInt("subscriber_id"), bookName, rs.getDate("borrowed_date").toString(), rs.getDate("expected_return_date").toString(), rs.getDate("return_date") != null ? rs.getDate("return_date").toString() : null);
                borrowedBooks.add(borrow);

            }
            if (borrowedBooks.isEmpty()) {
                System.out.println("No borrowed books found (showSubscriberBorrowedBooks)");
                return null;
            }
            System.out.println("Borrowed books found (showSubscriberBorrowedBooks)");
            return borrowedBooks;
        } catch (SQLException e) {
            System.out.println("Error: With exporting borrowed books from sql (showSubscriberBorrowedBooks) " + e);
            return null;
        }
    }

    /**
     * case 212
     * This method extends the return date of a borrowed book
     *
     * @param messageContent Array list containing the subscriber ID and the book ID and the new return date
     * @return Array list containing true if the book was reserved successfully and false if not with the error message
     */
    public ArrayList<String> extendBookBorrowTimeSubscriber(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();

        int subscriberId = Integer.parseInt(messageContent.get(0));
        int copyId = Integer.parseInt(messageContent.get(1));
        Date newReturnDate = null;

        try {
            conn.setAutoCommit(false);

            /*
             * Checks if the subscriber is frozen
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
            else{
                response.add("false");
                response.add("Subscriber not found");
                return response;
            }

            /*
             * Checks if less than a week is left for the book to be returned (book can be extended only if less than a week is left)
             */

            String checkReturnDateQuery = "SELECT DATEDIFF(expected_return_date, NOW()) AS days_diff , expected_return_date FROM borrow WHERE subscriber_id = ? AND copy_id = ? AND status = 'borrowed'";
            PreparedStatement checkReturnDateStatement = conn.prepareStatement(checkReturnDateQuery);
            checkReturnDateStatement.setInt(1, subscriberId);
            checkReturnDateStatement.setInt(2, copyId);
            ResultSet checkReturnDateRs = checkReturnDateStatement.executeQuery();
            if (checkReturnDateRs.next()) {
                LocalDate localDate = checkReturnDateRs.getDate("expected_return_date").toLocalDate().plusDays(7);
                newReturnDate = Date.valueOf(localDate);
                int daysDiff = checkReturnDateRs.getInt("days_diff");
                if (daysDiff > 7) {
                    response.add("false");
                    response.add("Cannot extend the return date due to having more than a week left");
                    return response;
                }
            }
            else{
                response.add("false");
                response.add("Borrow not found");
                return response;
            }

            /*
             * Checks if the book is reserved (ordered)
             */

            String checkReservedQuery = "SELECT reserved_copies FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
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
            else{
                response.add("false");
                response.add("Book not found");
                return response;
            }

            /*
             * The query updates the expected return date of the borrow table where the subscriber ID and the book ID matches the given values
             */

            String extendQuery = "UPDATE borrow SET expected_return_date = ?, notified = ? WHERE subscriber_id = ? AND copy_id = ? AND status = 'borrowed'";
            PreparedStatement extendStatement = conn.prepareStatement(extendQuery);
            extendStatement.setDate(1, newReturnDate);
            extendStatement.setInt(2, 0);
            extendStatement.setInt(3, subscriberId);
            extendStatement.setInt(4, copyId);
            extendStatement.executeUpdate();

            /*
             * Get the book name
             */

            String getBookNameQuery = "SELECT name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
            getBookNameStatement.setInt(1, copyId);
            ResultSet getBookNameRs = getBookNameStatement.executeQuery();
            String bookName = "";
            if (getBookNameRs.next()) {
                bookName = getBookNameRs.getString("name");
            }

            /*
             * Update subscriber history
             */

            updateHistory(subscriberId, "extend", "Return date for the book: " + bookName + " copy ID: " + copyId + " was extended by an extra week");

            /*
             * Send a notification to the librarians
             */

            String notifyQuery = "Insert into messages_for_librarians (message_date, message_info) values (?, ?)";
            PreparedStatement notifyStatement = conn.prepareStatement(notifyQuery);
            notifyStatement.setDate(1, Date.valueOf(LocalDate.now()));
            notifyStatement.setString(2, "Subscriber with ID: " + subscriberId + " has extended the return date for the book " + bookName + " for an extra week" + "book copy ID: " + copyId);
            notifyStatement.executeUpdate();

            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");
        } catch (Exception e) {

            /*
             * If an error occur rollback the transaction
             */

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            response.add("false");
            response.add("Problem with extending the return date");
            System.out.println("Error: With extending the return date (extendReturnDate) " + e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 216
     * This method edits the subscriber details
     *
     * @param messageContent the new subscriber details
     * @return array list containing true if the subscriber was edited successfully and false if not with the error message
     */
    public ArrayList<String> editSubscriberDetails(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        try {
            conn.setAutoCommit(false);

            int subscriberId = Integer.parseInt(messageContent.get(0));
            String newSubscriberEmail = messageContent.get(1);
            String newSubscriberPhoneNumber = messageContent.get(2);
            String newSubscriberFirstName = messageContent.get(3);
            String newSubscriberLastName = messageContent.get(4);

            /*
             * The query updates the phone number and email of the subscriber where the id matches the given value
             */

            String subscriberInfoQuery = "UPDATE subscriber SET phone_number = ?, email = ?, first_name = ?, last_name = ? WHERE subscriber_id = ?";
            PreparedStatement subscriberInfoStatement = conn.prepareStatement(subscriberInfoQuery);
            subscriberInfoStatement.setString(1, newSubscriberEmail);
            subscriberInfoStatement.setString(2, newSubscriberPhoneNumber);
            subscriberInfoStatement.setString(3, newSubscriberFirstName);
            subscriberInfoStatement.setString(4, newSubscriberLastName);
            subscriberInfoStatement.setInt(5, subscriberId);
            subscriberInfoStatement.executeUpdate();
            response.add("true");
            response.add(newSubscriberEmail);
            response.add(newSubscriberPhoneNumber);
            response.add(newSubscriberFirstName);
            response.add(newSubscriberLastName);

            /*
             * Update subscriber history
             */

            updateHistory(subscriberId, "edit", "Subscriber details were updated");

            /*
             * Commit the transaction
             */

            conn.commit();

        } catch (Exception e) {

            /*
             * If an error occur rollback the transaction
             */

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            response.add("false");
            response.add("Problem with updating the subscriber details");
            System.out.println("Error: With updating the subscriber (editSubscriberDetails) " + e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 218
     * This method edits the subscriber login details
     *
     * @param messageContent the new subscriber details
     * @return array list containing true if the subscriber was edited successfully and false if not with the error message
     */
    public ArrayList<String> editSubscriberPassword(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        try {
            conn.setAutoCommit(false);

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

            /*
             * Update subscriber history
             */

            updateHistory(subscriberId, "edit", "Subscriber password was updated");

            /*
             * Commit the transaction
             */

            conn.commit();
        } catch (SQLException e) {

            /*
             * If an error occurs, rollback the transaction
             */

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            response.add("false");
            response.add("Problem with updating the subscriber password");
            System.out.println("Error: With updating the subscriber (editSubscriberDetails) " + e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 220
     * This return an array list of all the copies of a specific book
     *
     * @param messageContent Array list containing the copyID
     * @return Array list containing the book copies if successful and null if not
     */
    public ArrayList<BookCopy> getBookCopies(String messageContent) {
        try {
            //! may be deleted later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            ArrayList<BookCopy> bookCopies = new ArrayList<>();
            int copyID = Integer.parseInt(messageContent);

            /*
             * The query selects all columns from the book_copy table where the serial number matches a given value
             */

            String findBookCopyQuery = "SELECT * FROM book_copy WHERE copy_id = ?";
            PreparedStatement findBookCopyStatement = conn.prepareStatement(findBookCopyQuery);
            findBookCopyStatement.setInt(1, copyID);
            ResultSet rs = findBookCopyStatement.executeQuery();

            /*
             * If the query was successful, add the values of the book to a list
             */

            while (rs.next()) {
                BookCopy bookCopy = new BookCopy(rs.getInt("copy_id"), rs.getInt("serial_number"), rs.getString("available"), rs.getString("shelf_location"));
                bookCopies.add(bookCopy);
            }
            if (bookCopies.isEmpty()) {
                System.out.println("No book copies found (getBookCopies)");
                return null;
            }
            System.out.println("Book copies found (getBookCopies)");
            return bookCopies;
        } catch (SQLException e) {
            System.out.println("Error: With exporting book copies from sql (getBookCopies) " + e);
            return null;
        }
    }

    /**
     * case 300
     * This method registers a new subscriber in the system
     *
     * @param messageContent the new subscriber details
     * @return array list containing true if the subscriber was added successfully and false if not with the error message
     */
    public ArrayList<String> registerNewSubscriber(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId;
            int historyId;

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
             * Create a new history record for the new subscriber
             */
            List<ArrayList<String>> history = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            temp.add(LocalDateTime.now().format(formatter));
            temp.add("register");
            temp.add("Subscriber was registered");
            history.add(temp);

            Blob historyBlob = convertToBlob(history);
            conn.setAutoCommit(false);

            /*
             * Create a new history record for the new subscriber
             */

            String historyQuery = "INSERT INTO subscription_history  (details) VALUES (?)"; // Adjust columns as needed
            PreparedStatement historyStatement = conn.prepareStatement(historyQuery, Statement.RETURN_GENERATED_KEYS);
            historyStatement.setBlob(1, historyBlob);
            historyStatement.executeUpdate();
            ResultSet generatedKeysHistoryId = historyStatement.getGeneratedKeys();
            if (generatedKeysHistoryId.next()) {
                historyId = generatedKeysHistoryId.getInt(1);
            } else {
                throw new SQLException("Error: Getting new history ID");
            }

            /*
             * Create a new user record for the new subscriber
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
             * Create a new subscriber record for the new subscriber
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

            /*
             * Commit the transaction
             */

            conn.commit();

            response.add("true");
            String registerInfo = String.format("%s %s was registered successfully\n ID: %s\n Username: %s temporary password: %s", messageContent.get(1), messageContent.get(2), subscriberId, messageContent.get(0), "Aa123456");
            response.add(registerInfo);

        } catch (SQLException e) {
            /*
             * If an error occur rollback the transaction
             */
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
     * @return Array list containing true if the book was borrowed successfully and false if not with the error message
     */
    public ArrayList<String> borrowBookToSubscriber(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        try {
            int subscriberId = Integer.parseInt(messageContent.get(0));
            int copyId = Integer.parseInt(messageContent.get(1));
            String bookName = "";

            /*
             * The query parses the return date from the message content
             */

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(messageContent.get(2), formatter);
            Date returnDate = Date.valueOf(localDate);


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
            else{
                response.add("false");
                response.add("Subscriber does not exist");
                return response;
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
            } else {
                response.add("false");
                response.add("Book copy was not found in the system");
                return response;
            }

            /*
             * Check if the book is reserved by the subscriber
             */

            boolean wasReserved = false;
            String bookReservedByTheSubscriberQuery = "SELECT subscriber_id, notify FROM reservation WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement bookReservedByTheSubscriberStatement = conn.prepareStatement(bookReservedByTheSubscriberQuery);
            bookReservedByTheSubscriberStatement.setInt(1, copyId);
            ResultSet bookReservedByTheSubscriberRs = bookReservedByTheSubscriberStatement.executeQuery();
            if (bookReservedByTheSubscriberRs.next()) {
                int firstSubscriberId = bookReservedByTheSubscriberRs.getInt("subscriber_id");
                boolean notify = bookReservedByTheSubscriberRs.getInt("notify") == 1;
                if (firstSubscriberId == subscriberId) {
                    if (notify) {
                        /*
                         * The subscriber was the first one to reserve the book and was notified
                         */
                        String deleteReservationQuery = "DELETE FROM reservation WHERE subscriber_id = ? AND serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                        PreparedStatement deleteReservationStatement = conn.prepareStatement(deleteReservationQuery);
                        deleteReservationStatement.setInt(1, subscriberId);
                        deleteReservationStatement.setInt(2, copyId);
                        deleteReservationStatement.executeUpdate();
                        wasReserved = true;
                    } else {
                        response.add("false");
                        response.add("Subscriber reserved the book but was not notified");
                        return response;
                    }
                }
            }

            System.out.println("wasReserved: " + wasReserved);

            /*
             * Check if the book is reserved
             */

            if (!wasReserved) {
                String checkReservedQuery = "SELECT copies, reserved_copies, borrowed_copies, name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                PreparedStatement checkReservedStatement = conn.prepareStatement(checkReservedQuery);
                checkReservedStatement.setInt(1, copyId);
                ResultSet checkReservedRs = checkReservedStatement.executeQuery();
                if (checkReservedRs.next()) {
                    int copies = checkReservedRs.getInt("copies");
                    int reservedCopies = checkReservedRs.getInt("reserved_copies");
                    int borrowedCopies = checkReservedRs.getInt("borrowed_copies");
                    bookName = checkReservedRs.getString("name");
                    if (copies <= (reservedCopies + borrowedCopies)) {
                        response.add("false");
                        response.add("Book is reserved");
                        return response;
                    }
                }
                else{
                    response.add("false");
                    response.add("Book not found");
                    return response;
                }
            }
            

            conn.setAutoCommit(false);

            /*
             * The query updates the status of the book_copy to unavailable (0)
             */

            String updateCopyQuery = "UPDATE book_copy SET available = ? WHERE copy_id = ?";
            PreparedStatement updateCopyStatement = conn.prepareStatement(updateCopyQuery);
            updateCopyStatement.setInt(1, 0);
            updateCopyStatement.setInt(2, copyId);
            updateCopyStatement.executeUpdate();
            


            /*
             * The query update the amount of borrowed books in the book table
             */

            String updateBookQuery = "UPDATE book SET borrowed_copies = borrowed_copies + 1 WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement updateBookStatement = conn.prepareStatement(updateBookQuery);
            updateBookStatement.setInt(1, copyId); // copy_id
            updateBookStatement.executeUpdate();
            

            /*
             * The query inserts the book details into the borrow table
             */

            String borrowQuery = "INSERT INTO borrow (copy_id, subscriber_id, expected_return_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement borrowStatement = conn.prepareStatement(borrowQuery);
            borrowStatement.setInt(1, copyId);
            borrowStatement.setInt(2, subscriberId);
            borrowStatement.setDate(3, returnDate);
            borrowStatement.setString(4, "borrowed");
            borrowStatement.executeUpdate();
            

            /*
             * Update subscriber history
             */
            DateTimeFormatter historyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String newReturnDate = returnDate.toLocalDate().format(historyFormatter);
            updateHistory(subscriberId, "borrow", "Borrowed the book: " + bookName + " copy ID: " + copyId + " Expected return date: " + newReturnDate);
            

            /*
             * Commit the transaction
             */

            conn.commit();

            response.add("true");
            response.add("Book " + bookName + " borrowed successfully to subscriber " + subscriberId);

        } catch (Exception e) {

            /*
             * If an error occur rollback the transaction
             */

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            System.out.println("Error: Borrowing book" + e);
            response.add("false");
            response.add("Book copy was not found in the system");


        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 304
     * This method returns a book from a subscriber
     *
     * @param messageContent Array list containing the subscriber ID and the book ID
     * @return Array list containing true if the book was returned successfully and false if not with the error message
     */
    public ArrayList<String> returnBookFromSubscriber(String messageContent) {
        ArrayList<String> response = new ArrayList<>();
        int subscriberId = 0;
        int copyId = Integer.parseInt(messageContent);

        try {
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

            String updateBookQuery = "UPDATE book SET borrowed_copies = borrowed_copies - 1" + " WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
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
            else{
                response.add("false");
                response.add("Book not found");
                return response;
            }

            /*
             * Get the book name
             */

            String getBookNameQuery = "SELECT name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
            getBookNameStatement.setInt(1, copyId);
            ResultSet getBookNameRs = getBookNameStatement.executeQuery();
            String bookName = "";
            if (getBookNameRs.next()) {
                bookName = getBookNameRs.getString("name");
            }
            else{
                response.add("false");
                response.add("Book not found");
                return response;
            }

            /*
             * Check if late return
             */

            Boolean lateReturn = false;
            String checkLateReturnQuery = "SELECT DATEDIFF(CURDATE(), expected_return_date) AS days_diff FROM borrow WHERE subscriber_id = ? AND copy_id = ? AND status = 'borrowed'";
            PreparedStatement checkLateReturnStatement = conn.prepareStatement(checkLateReturnQuery);
            checkLateReturnStatement.setInt(1, subscriberId);
            checkLateReturnStatement.setInt(2, copyId);
            ResultSet checkLateReturnRs = checkLateReturnStatement.executeQuery();
            if (checkLateReturnRs.next()) {
                int daysDiff = checkLateReturnRs.getInt("days_diff");
                if (daysDiff >= 7) {
                    lateReturn = true;
                    // Freeze the subscriber account for a month
                    freezeAccount(subscriberId, "Late return of book " + bookName);
                }
            }

            /*
             * The query updates the status of the borrow table to returned and the return date to the current date
             */

            String returnQuery = "UPDATE borrow SET status = ?, return_date = ? WHERE subscriber_id = ? AND copy_id = ? AND status = 'borrowed'";
            PreparedStatement returnStatement = conn.prepareStatement(returnQuery);
            returnStatement.setString(1, "returned");
            returnStatement.setDate(2, Date.valueOf(LocalDate.now()));
            returnStatement.setInt(3, subscriberId);
            returnStatement.setInt(4, copyId);
            returnStatement.executeUpdate();


            /*
             * Update subscriber history
             */

            if(lateReturn){
                updateHistory(subscriberId, "late return", "Book: " + bookName + " of copy ID: " + copyId + " was returned late account is frozen for a month");
            }
            else{
                updateHistory(subscriberId, "return", "Book: " + bookName + " of copy ID: " + copyId + " was returned");
            }

            checkReservation(bookName);

            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");
            if (lateReturn) {
                response.add("Book " + bookName + " returned successfully to subscriber " + subscriberId + " but it was late account is frozen for a month");
            }
            else{
                response.add("Book " + bookName + " returned successfully to subscriber " + subscriberId);
            }

        } catch (Exception e) {

            /*
             * If an error occur rollback the transaction
             */

            try {
                conn.rollback();
            } catch (Exception rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            System.out.println("Error: Returning book" + e);
            response.add("false");
            response.add("Problem with returning the book");

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * case 306
     * This method gets the list of all subscribers in the system
     *
     * @return The arraylist of all subscribers
     */
    public ArrayList<Subscriber> viewAllSubscribers() {
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

            /*
             * Subscriber/s found
             */

            if (!subscribersList.isEmpty()) {
                System.out.println("Subscribers list found (viewAllSubscribers)");
                return subscribersList;

            }

            /*
             * No subscriber found
             */

            else {
                System.out.println("No subscribers found (viewAllSubscribers)");
                return null;
            }

        } catch (SQLException e) {

            /*
             * If an error occur
             */

            System.out.println("Error: With exporting subscribers from sql(viewAllSubscribers) " + e);
            return null;
        }
    }

    /**
     * case 308
     * This method returns a specific subscriber's details to the librarian by the subscriber ID
     *
     * @param subscriberId The subscriber ID
     * @return The subscriber
     */
    public Subscriber viewSubscriberDetails(int subscriberId) {
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

            /*
             * No subscriber found
             */

            else {
                System.out.println("No subscriber found (viewSubscriberDetails)");
                return null;
            }
        } catch (SQLException e) {

            /*
             * If an error occur
             */

            System.out.println("Error: With exporting subscriber from sql(viewSubscriberDetails) " + e);
            return null;
        }
    }

    /**
     * case 310
     * This method extends the return date of a borrowed book (librarian)
     *
     * @param messageContent Array list containing the subscriber ID, the book ID, the new return date and the librarian ID
     * @return Array list containing true if the book was extended successfully and false if not with the error message
     */
    public ArrayList<String> extendBookBorrowTimeLibrarian(ArrayList<String> messageContent) {
        ArrayList<String> response = new ArrayList<>();
        int subscriberId = Integer.parseInt(messageContent.get(0));
        int copyId = Integer.parseInt(messageContent.get(1));
        Date newReturnDate;

        try {
            conn.setAutoCommit(false);
            /*
             * Checks if the subscriber is frozen
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
            else{
                response.add("false");
                response.add("Subscriber does not exist");
                return response;
            }

            /*
             * Checks if the book is reserved (ordered)
             */

            String checkReservedQuery = "SELECT reserved_copies FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
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
            else{
                response.add("false");
                response.add("Book not found");
                return response;
            }

            /*
             * The query parses the return date from the message content
             */

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(messageContent.get(2), formatter);
            newReturnDate = Date.valueOf(localDate);

            /*
             * The query updates the expected return date of the borrow table where the subscriber ID and the book ID matches the given values
             * and sets the notify column to 0
             * and the copy is borrowed
             */

            String extendQuery = "UPDATE borrow SET expected_return_date = ?, notified = ? WHERE subscriber_id = ? AND copy_id = ? AND status = 'borrowed'";
            PreparedStatement extendStatement = conn.prepareStatement(extendQuery);
            extendStatement.setDate(1, newReturnDate);
            extendStatement.setInt(2, 0);
            extendStatement.setInt(3, subscriberId);
            extendStatement.setInt(4, copyId);
            extendStatement.executeUpdate();

            /*
             * Gets the librarian first name and last name
             */

            String getLibrarianNameQuery = "SELECT first_name, last_name FROM librarian WHERE librarian_id = ?";
            PreparedStatement getLibrarianNameStatement = conn.prepareStatement(getLibrarianNameQuery);
            getLibrarianNameStatement.setInt(1, Integer.parseInt(messageContent.get(3)));
            ResultSet getLibrarianNameRs = getLibrarianNameStatement.executeQuery();
            String librarianName = "";
            if (getLibrarianNameRs.next()) {
                librarianName = getLibrarianNameRs.getString("first_name") + " " + getLibrarianNameRs.getString("last_name");
            }
            else{
                response.add("false");
                response.add("Librarian not found");
                return response;
            }

            /*
             * Get book name
             */
            String getBookNameQuery = "SELECT name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
            PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
            getBookNameStatement.setInt(1, copyId);
            ResultSet getBookNameRs = getBookNameStatement.executeQuery();
            String bookName = "";
            if (getBookNameRs.next()) {
                bookName = getBookNameRs.getString("name");
            }

            /*
             * Update subscriber history
             */

            updateHistory(subscriberId, "extend", "Return date for the book: " + bookName + " copy ID: " + copyId + " was extended by librarian " + librarianName + " to " + newReturnDate);

            /*
             * Commit the transaction
             */

            conn.commit();
            response.add("true");

        } catch (SQLException e) {
            /*
             * If an error occur rollback the transaction
             */
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error: Rolling back transaction" + rollbackEx);
            }
            response.add("false");
            response.add("Problem with extending the return date");
            System.out.println("Error: With extending the return date (extendReturnDate) " + e);
        } finally {
            try {

                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error: Setting auto-commit back to true" + ex);
            }
        }
        return response;
    }

    /**
     * This method check if a subscribe need to be notified about a reserved book
     * if the subscriber has a reserved book and the book is available, the subscriber will be notified
     *
     * @param bookName The subscriber details
     */
    public void checkReservation(String bookName) {
        try {
            int subscriberId;
            String checkReservationQuery = "SELECT subscriber_id, notify FROM reservation WHERE serial_number = (SELECT serial_number FROM book WHERE name = ?) ORDER BY reserve_date ASC LIMIT 1";
            PreparedStatement checkReservationStatement = conn.prepareStatement(checkReservationQuery);
            checkReservationStatement.setString(1, bookName);
            ResultSet checkReservationRs = checkReservationStatement.executeQuery();
            if (checkReservationRs.next()) {
                subscriberId = checkReservationRs.getInt("subscriber_id");
                boolean notify = checkReservationRs.getInt("notify") == 1;
                if (!notify) {

                    /*
                     * send a notification to the subscriber
                     */

                    String GetSubscriberEmailQuery = "SELECT email FROM subscriber WHERE subscriber_id = ?";
                    PreparedStatement GetSubscriberEmailStatement = conn.prepareStatement(GetSubscriberEmailQuery);
                    GetSubscriberEmailStatement.setInt(1, subscriberId);
                    ResultSet GetSubscriberEmailRs = GetSubscriberEmailStatement.executeQuery();
                    if (GetSubscriberEmailRs.next()) {
                        String email = GetSubscriberEmailRs.getString("email");
                        notificationController.sendEmail(email, "Book available", "The book " + bookName + " is now available for you to borrow it will be reserved for you until the end of " + LocalDate.now().plusDays(2));
                    }

                    /*
                     * Update the reservation
                     */

                    String updateReservationQuery = "UPDATE reservation SET notify = 1, notify_date = ? WHERE subscriber_id = ? AND serial_number = (SELECT serial_number FROM book WHERE name = ?)";
                    PreparedStatement updateReservationStatement = conn.prepareStatement(updateReservationQuery);
                    updateReservationStatement.setDate(1, Date.valueOf(LocalDate.now()));
                    updateReservationStatement.setInt(2, subscriberId);
                    updateReservationStatement.setString(3, bookName);
                    updateReservationStatement.executeUpdate();

                    // Update subscriber history
                    updateHistory(subscriberId, "notified", "that the book " + bookName + " is available");

                }
            }
        } catch (Exception e) {
            System.out.println("Error: Checking the reservation" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method converts a Blob to a List<ArrayList<String>>
     *
     * @param blob The Blob
     * @return List<ArrayList < String>>
     * @throws IOException            If an error occurs
     * @throws SQLException           If an error occurs
     * @throws ClassNotFoundException If an error occurs
     */
    public static List<ArrayList<String>> convertBlobToList(Blob blob) throws IOException, SQLException, ClassNotFoundException {
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<ArrayList<String>>) obj;
            } else {
                throw new ClassCastException("Deserialized object is not of type List<ArrayList<String>>");
            }
        } catch (EOFException e) {
            throw new IOException("Error: Unexpected end of file while reading Blob data", e);
        }
    }

    /**
     * This method converts a List<ArrayList<String>> to a Blob
     *
     * @param data The List<ArrayList<String>> data
     * @return Blob
     * @throws IOException  If an error occurs
     * @throws SQLException If an error occurs
     */
    public static Blob convertToBlob(List<ArrayList<String>> data) throws IOException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(data);
        }
        byte[] bytes = baos.toByteArray();
        return new SerialBlob(bytes);
    }

    /**
     * This method writes new data into a List<ArrayList<String>>
     *
     * @param data    The List<ArrayList<String>> data
     * @param newData The new data to be added
     */
    public static void writeDataToList(List<ArrayList<String>> data, ArrayList<String> newData) {
        data.add(newData);
    }


    /**
     * This method updates the history of a subscriber
     *
     * @param subscriberId The ID of the subscriber
     * @param action       The action that was performed
     * @param details      The details of the action
     */
    public void updateHistory(int subscriberId, String action, String details) {
        {
            try {
                Blob updatedHistoryBlob;
                List<ArrayList<String>> history = null;
                ArrayList<String> newData = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                newData.add(LocalDateTime.now().format(formatter));
                newData.add(action);
                newData.add(details);



                /*
                 * Get the history of the subscriber
                 */

                String getHistoryQuery = "SELECT details from subscription_history WHERE subscription_history_id = (SELECT detailed_subscription_history FROM subscriber WHERE subscriber_id = ?)";
                PreparedStatement getHistoryStatement = conn.prepareStatement(getHistoryQuery);
                getHistoryStatement.setInt(1, subscriberId);
                ResultSet getHistoryRs = getHistoryStatement.executeQuery();
                if (getHistoryRs.next()) {
                    Blob historyBlob = getHistoryRs.getBlob("details");
                    history = convertBlobToList(historyBlob);
                    System.out.println("History found" + history);
                    //Write the new data into the history
                    writeDataToList(history, newData);
                }

                /*
                 * Convert the csv file to a blob
                 */
                if (history != null) {
                    updatedHistoryBlob = convertToBlob(history);

                    /*
                     * Update the history of the subscriber
                     */

                    String updateHistoryQuery = "UPDATE subscription_history SET details = ? WHERE subscription_history_id = (SELECT detailed_subscription_history FROM subscriber WHERE subscriber_id = ?)";
                    PreparedStatement updateHistoryStatement = conn.prepareStatement(updateHistoryQuery);
                    updateHistoryStatement.setBlob(1, updatedHistoryBlob);
                    updateHistoryStatement.setInt(2, subscriberId);
                    updateHistoryStatement.executeUpdate();
                }

            } catch (Exception e) {
                System.out.println("Error: Updating the history" + e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method freezes a subscriber account
     *
     * @param subscriberId The ID of the subscriber
     * @param reason       The reason for freezing the account
     */
    public void freezeAccount(int subscriberId, String reason) {
        try {
            /*
             * The query updates the status of the subscriber to frozen (1)
             */

            String freezeQuery = "UPDATE subscriber SET status = ?,frozen_date = ? WHERE subscriber_id = ?";
            PreparedStatement freezeStatement = conn.prepareStatement(freezeQuery);
            freezeStatement.setInt(1, 1);
            freezeStatement.setInt(2, subscriberId);
            freezeStatement.setDate(3, Date.valueOf(LocalDate.now()));
            freezeStatement.executeUpdate();
            // Update subscriber history
            updateHistory(subscriberId, "frozen", "Account was frozen reason: " + reason);
        } catch (SQLException e) {
            System.out.println("Error: Freezing account" + e);
        }
    }

    /**
     * This method unfreezes a subscriber account
     */
    public Runnable unfreezeAccount() {
        return () -> {
            try {
                /*
                 * Get the subscriber ID to unfreeze
                 */

                String getSubscriberIdQuery = "SELECT subscriber_id ,DATEDIFF(NOW(), frozen_date) AS frozen_time FROM subscriber WHERE status = 1";
                PreparedStatement getSubscriberIdStatement = conn.prepareStatement(getSubscriberIdQuery);
                ResultSet getSubscriberIdRs = getSubscriberIdStatement.executeQuery();
                while (getSubscriberIdRs.next()) {
                    if (getSubscriberIdRs.getInt("frozen_time") >= 30) {
                        {
                            /*
                             * Unfreeze the subscribers
                             */
                            int subscriberId = getSubscriberIdRs.getInt("subscriber_id");
                            String unfreezeQuery = "UPDATE subscriber SET status = 0 WHERE subscriber_id = ?";
                            PreparedStatement unfreezeStatement = conn.prepareStatement(unfreezeQuery);
                            unfreezeStatement.setInt(1, subscriberId);
                            unfreezeStatement.executeUpdate();
                            // Update subscriber history
                            updateHistory(subscriberId, "unfrozen", "Account was unfrozen");
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error: Unfreezing account" + e);
            }
        };
    }


    /**
     * This method checks if a book is due for return
     *
     * @return Runnable
     */
    public Runnable checkDueBooks() {
        return () -> {
            try {

                /*
                 * Check if the subscribers has to return a book
                 */

                String query = "SELECT subscriber_id, copy_id, expected_return_date, DATEDIFF(expected_return_date, NOW()) AS date_diff FROM borrow WHERE DATEDIFF(expected_return_date, NOW()) <= 1 AND status = 'borrowed'";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int subscriberId = rs.getInt("subscriber_id");
                    int copyId = rs.getInt("copy_id");
                    Date expectedReturnDate = rs.getDate("expected_return_date");
                    int dateDiff = rs.getInt("date_diff");
                    String bookName = "";

                    /*
                     * Get the book name
                     */

                    String getBookNameQuery = "SELECT name FROM book WHERE serial_number = (SELECT serial_number FROM book_copy WHERE copy_id = ?)";
                    PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
                    getBookNameStatement.setInt(1, copyId);
                    ResultSet getBookNameRs = getBookNameStatement.executeQuery();
                    if (getBookNameRs.next()) {
                        bookName = getBookNameRs.getString("name");
                    }

                    /*
                     * Get the subscriber email
                     */

                    String getEmailQuery = "SELECT email FROM subscriber WHERE subscriber_id = ?";
                    PreparedStatement getEmailStatement = conn.prepareStatement(getEmailQuery);
                    getEmailStatement.setInt(1, subscriberId);
                    ResultSet emailRs = getEmailStatement.executeQuery();
                    if (emailRs.next()) {
                        String email = emailRs.getString("email");
                        if (dateDiff < 0 && dateDiff > -7) {
                            notificationController.sendEmail(email, "Book Return Reminder", "Please return the book " + bookName + " with copy ID " + copyId + " else account will be frozen in " + (7 - Math.abs(dateDiff)) + " days");
                        } else if (dateDiff > 0) {
                            notificationController.sendEmail(email, "Book Return Reminder", "Please return the book " + bookName + " with copy ID " + copyId + " due date is in " + dateDiff + " days");
                        }
                    }

                    /*
                     * Update subscriber history
                     */

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedExpectedDate = expectedReturnDate.toLocalDate().format(formatter);
                    updateHistory(subscriberId, "notified", "Reminder to return the book with copy ID " + copyId + " by " + formattedExpectedDate + ". Days left: " + dateDiff);
                }
            } catch (SQLException e) {
                System.out.println("Error: Checking due books" + e);
            }
        };
    }

    /**
     * This method checks if a reservation is due
     *
     * @return Runnable
     */
    public Runnable checkReservationDue() {
        return () -> {
            try {
                /*
                 * Check if the subscriber was notified
                 */

                String checkNotificationQuery = "SELECT subscriber_id, serial_number, DATEDIFF(NOW(), notify_date) AS date_diff FROM reservation WHERE notify = 1";
                PreparedStatement checkNotificationStatement = conn.prepareStatement(checkNotificationQuery);
                ResultSet checkNotificationRs = checkNotificationStatement.executeQuery();
                while (checkNotificationRs.next()) {
                    int subscriberId = checkNotificationRs.getInt("subscriber_id");
                    int serialNumber = checkNotificationRs.getInt("serial_number");
                    String email = "";
                    int dateDiff = checkNotificationRs.getInt("date_diff");
                    String bookName = "";

                    /*
                     *Get the book name
                     */

                    String getBookNameQuery = "SELECT name FROM book WHERE serial_number = ?";
                    PreparedStatement getBookNameStatement = conn.prepareStatement(getBookNameQuery);
                    getBookNameStatement.setInt(1, serialNumber);
                    ResultSet getBookNameRs = getBookNameStatement.executeQuery();
                    if (getBookNameRs.next()) {
                        bookName = getBookNameRs.getString("name");
                    }

                    /*
                     * Get the subscriber email
                     */

                    String getEmailQuery = "SELECT email FROM subscriber WHERE subscriber_id = ?";
                    PreparedStatement getEmailStatement = conn.prepareStatement(getEmailQuery);
                    getEmailStatement.setInt(1, subscriberId);
                    ResultSet emailRs = getEmailStatement.executeQuery();
                    if (emailRs.next()) {
                        email = emailRs.getString("email");
                    }

                    /*
                     * check if the subscriber was notified and the date difference
                     */
                    if (dateDiff <= 2) {
                        // Notify the subscriber
                        notificationController.sendEmail(email, "Book Reservation Reminder", "Reminder to borrow the book " + bookName + ".\nDays left until reservation is canceled: " + (2 - dateDiff));
                        // Update subscriber history
                        updateHistory(subscriberId, "notified", "Reminder to borrow the book " + bookName + ". \nDays left until reservation is canceled: " + (2 - dateDiff));
                    } else {
                        System.out.println("Reservation canceled " + subscriberId + " " + serialNumber);
                        String deleteReservationQuery = "DELETE FROM reservation WHERE subscriber_id = ? AND serial_number = ?";
                        PreparedStatement deleteReservationStatement = conn.prepareStatement(deleteReservationQuery);
                        deleteReservationStatement.setInt(1, subscriberId);
                        deleteReservationStatement.setInt(2, serialNumber);
                        deleteReservationStatement.executeUpdate();
                        // Notify the subscriber
                        notificationController.sendEmail(email, "Reservation Canceled", "Reservation for the book " + bookName + " was canceled due to inactivity");
                        // Update subscriber history
                        updateHistory(subscriberId, "reservation canceled", "Reservation for the book " + bookName + " was canceled");
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error: Checking due books" + e);
            }
        };

    }
}