package logic;


import common.ClientServerMessage;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class extends the AbstractServer class and implements the SubscriberController and LibrarianController interfaces.
 * It is used to control the server.
 */
public class ServerController extends AbstractServer {

    private final ServerMonitorFrameController serverMonitorController;
    private DBController dbController = null;
    //private static NotificationController notificationController;


    public ServerController(int port, ServerMonitorFrameController serverMonitorController) {
        super(port);
        this.serverMonitorController = serverMonitorController;
        //NotificationController notificationController = NotificationController.getInstance();

    }

    /**
     * This method overrides the one in the superclass. Called
     * when a client has connected to the server add it to the list of clients.
     *
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected" + client.toString());
        serverMonitorController.clientConnected(client);
    }

    /**
     * This method overrides the one in the superclass. Called
     * when a client has disconnected from the server remove it from the list of clients.
     *
     * @param client the connection with the client.
     */
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected");
        serverMonitorController.clientDisconnected(client);
    }

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client. (msg contains the id of the message and the message content)
     * @param client The connection from which the message originated.
     * @param
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try {
            if (msg instanceof ClientServerMessage) {

                /*
                 * 100 - user wants to log in to his account
                 * 102 - user closed the app
                 * 200 - subscriber wants to search a book by its name
                 * 202 - subscriber wants to search a book by its genre
                 * 204 - subscriber wants to search a book by its description
                 * 206 - subscriber wants to check book availability of a specific book
                 * 208 - subscriber wants to order a book
                 * 210 - subscriber wants to see his borrowed book list
                 * 212 - subscriber wants to extend his borrow time
                 * 214 - subscriber wants to view his subscription history
                 * 216 - subscriber wants to edit his personal information
                 * 218 - subscriber wants to edit his login information
                 * 300 - librarian wants to register a new subscriber into the system
                 * 302 - librarian borrows a book to a subscriber
                 * 304 - librarian returns a book of a subscriber to the library
                 * 306 - librarian wants to view subscribers in the system
                 * 308 - librarian wants to view details of a specific subscriber
                 * 310 - librarian wants to extend the borrow time for a book subscriber that was borrowed by a subscriber
                 * 312 - librarian wants to view a list of all logs
                 */
                ClientServerMessage message = (ClientServerMessage) msg;
                switch (message.getId()) {
                    case (100):
                        /**
                         * do: user wants to log in to his account
                         * in: arraylist<username,password>
                         * return:(id 101) subscriber / librarian base of the username
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                // get the user details from the database
                                client.sendToClient(new ClientServerMessage(101, dbController.userLogin((ArrayList<String>) message.getMessageContent())));
                                System.out.println("user details were sent to client");

                            }
                            // message type isn't an arraylist
                            else {
                                client.sendToClient(new ClientServerMessage(101, null));
                                System.out.println("Cannot log in the account - message is not an ArrayList<String> (case 100)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(101, null));
                            System.out.println("Error: with user login method (case 100) " + e);
                        }
                        break;
                    case (102):
                        /**
                         * Do: disconnect client from server
                         */
                        serverMonitorController.clientDisconnected(client);
                        client.sendToClient(null); // send null to client to make him stop waiting for a response
                        System.out.println("Client " + client + " disconnected");
                        break;
                    case (200):
                        /**
                         * do: subscriber wants to search a book by its name
                         * in: string
                         * return: (id 201) arraylist<book>
                         */
                        if (message.getMessageContent() instanceof String) {
                            try {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByName((String) message.getMessageContent())));
                                System.out.println("Books search by name was sent to client");
                            } catch (Exception e) {
                                System.out.println("Error: with getting book search by name (case 200)" + e);
                                client.sendToClient(new ClientServerMessage(201, null));
                            }
                        } else {
                            System.out.println("Cannot search book by name - message is not a string (case 200)");
                            client.sendToClient(new ClientServerMessage(201, null));

                        }
                        break;
                    case (202):
                        /**
                         * do: subscriber wants to search a book by its genre
                         * in: string
                         * return: (id 201) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByGenre((String) message.getMessageContent())));
                                System.out.println("Books search by genre was sent to client");
                            } else {
                                System.out.println("Cannot search book by genre - message is not a string (case 202)");
                                client.sendToClient(new ClientServerMessage(201, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting book search by genre (case 202)" + e);
                            client.sendToClient(new ClientServerMessage(201, null));
                        }
                        break;
                    case (204):
                        /**
                         * do: subscriber wants to search a book by its description
                         * in: string
                         * return: (id 201) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByDescription((String) message.getMessageContent())));
                                System.out.println("Books search by description was sent to client");
                            } else {
                                System.out.println("Cannot search book by description - message is not a string (case 204)");
                                client.sendToClient(new ClientServerMessage(201, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting book search by description (case 204)" + e);
                            client.sendToClient(new ClientServerMessage(201, null));
                        }
                        break;
                    case (206):
                        /**
                         * do: subscriber wants to  check book availability of a specific book
                         * in: int
                         * return: (id 207) book
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(207, dbController.checkBookAvailability((Integer) message.getMessageContent())));
                                System.out.println("Book details were sent to client");
                            } else {
                                System.out.println("Cannot get book details - message is not an integer (case 206)");
                                client.sendToClient(new ClientServerMessage(207, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting book details (case 206)" + e);
                            client.sendToClient(new ClientServerMessage(207, null));
                        }
                        break;
                    case (208):
                        /**
                         * do: subscriber wants to reserve a book
                         * in: ArrayList<String> {subscriber id, book id}
                         * return: (id 209) arraylist<string> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(209, dbController.reserveBook((ArrayList<String>) message.getMessageContent())));
                                System.out.println("Book was reserved");
                            } else {
                                client.sendToClient(new ClientServerMessage(209, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot reserve book Message is not an ArrayList<String>");
                                }}));
                                System.out.println("Cannot reserve book Message is not an ArrayList<String> (case 208)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with reserve method (case 208)" + e);
                            client.sendToClient(new ClientServerMessage(209, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                        }
                        break;
                    case (210):
                        /**
                         * do: subscriber wants to see his borrowed book list
                         * in: int
                         * return: (id 211) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(211, dbController.showSubscriberBorrowedBooks((Integer) message.getMessageContent())));
                                System.out.println("Subscriber borrowed books were sent to client");
                            } else {
                                System.out.println("Cannot find borrowed books - message is not an integer (case 210)");
                                client.sendToClient(new ClientServerMessage(211, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting borrowed book details (case 210)" + e);
                            client.sendToClient(new ClientServerMessage(211, null));
                        }
                        break;
                    case (212):
                        /**
                         * do: subscriber wants to extend his borrow time
                         * in: ArrayList<String> {subscriber id, copy id}
                         * return: (id 213) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(213, dbController.extendBookBorrowTimeSubscriber((ArrayList<String>) message.getMessageContent())));
                                System.out.println("Borrow time was extended");
                            } else {
                                client.sendToClient(new ClientServerMessage(213, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot extend borrow time Message is not an ArrayList<String>");
                                }}));
                                System.out.println("Cannot extend borrow time Message is not an ArrayList<String> (case 212)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with extend borrow time method (case 212)" + e);
                            client.sendToClient(new ClientServerMessage(213, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                        }
                        break;
                    case (214):
                        break;
                    case (216):
                        /**
                         * do: subscriber wants to edit his personal information
                         * in: ArrayList<String>
                         * return: (id 217) arraylist<string> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(217, dbController.editSubscriberDetails((ArrayList<String>) message.getMessageContent())));
                                System.out.println("Subscriber details were edited");
                            } else {
                                client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                    add("False");
                                    add("Cannot Edit account Message is not ArrayList<String> (case 216)");
                                }}));
                                System.out.println("Cannot Edit account Message is not a subscriber (case 216)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case 216)" + e);
                            client.sendToClient(new ClientServerMessage(217, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 216)");
                            }}));
                        }
                        break;
                    case (218):
                        /**
                         * do: subscriber wants to edit his login information
                         * in: int
                         * return: (id 217) arraylist<string> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(219, dbController.editSubscriberPassword((ArrayList<String>) message.getMessageContent())));
                                System.out.println("Subscriber login details were edited");
                            } else {
                                client.sendToClient(new ClientServerMessage(219, new ArrayList<String>() {{
                                    add("False");
                                    add("Cannot view account Message is not an ArrayList<String> (case 218)");
                                }}));
                                System.out.println("Cannot view account Message is not an ArrayList<String> (case 218)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with Edit method (case 218)" + e);
                            client.sendToClient(new ClientServerMessage(219, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 218)");
                            }}));
                        }
                        break;
                    case(220):
                        /**
                         * do: Returns an array list of all of the book copies of a specific book
                         * in: String {copyID}
                         * return: (id 221) ArrayList<BookCopy> {book copies}
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(221, dbController.getBookCopies((String) message.getMessageContent())));
                                System.out.println("Book copies were sent to client");
                            } else {
                                System.out.println("Cannot get book copies Message is not a String (case 220)");
                                client.sendToClient(new ClientServerMessage(221, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting book copies (case 220)" + e);
                            client.sendToClient(new ClientServerMessage(221, null));
                        }
                        break;
                    case (300):
                        /**
                         * do: librarian wants to register a new subscriber into the system
                         * in: ArrayList<String> {subscriber details}
                         * return: (id 301) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(301, dbController.registerNewSubscriber((ArrayList<String>) message.getMessageContent())));
                            } else {
                                System.out.println("Cannot register account Message is not an ArrayList<String> (case 300)");
                                client.sendToClient(new ClientServerMessage(301, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot register account Message is not an ArrayList<String> (case 300)");
                                }}));
                            break;
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with register method (case 300)" + e);
                            client.sendToClient(new ClientServerMessage(301, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 300)");
                            }}));
                        }
                        break;
                    case (302):
                        /**
                         * do: librarian borrows a book to a subscriber
                         * in: ArrayList<String> {subscriber id, book id, return date}
                         * return: (id 303) ArrayList<String> {success/fail, error message}
                         */
                        ArrayList<String> response = null;
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                response = dbController.borrowBookToSubscriber((ArrayList<String>) message.getMessageContent());
                                client.sendToClient(new ClientServerMessage(303, response));
                                System.out.println("message was sent to client (case 302)" + response);
                            } else {
                                response = new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot borrow book Message is not an ArrayList<String> (case 302)");
                                }};
                                client.sendToClient(new ClientServerMessage(303, response));
                                System.out.println("message was sent to client (case 302)" + response);
                            }
                        } catch (Exception e) {
                            response = new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 302)");
                            }};
                            client.sendToClient(new ClientServerMessage(303, response));
                            System.out.println("Error: with borrow method (case 302)" + e);
                        }
                        break;
                    case (304):
                        /**
                         * do: librarian returns a book of a subscriber to the library
                         * in: String {book id}
                         * return: (id 305) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(305, dbController.returnBookFromSubscriber((String) message.getMessageContent())));
                                System.out.println("Book was returned from subscriber");
                            } else {
                                System.out.println("Cannot return book Message is not a String");
                                client.sendToClient(new ClientServerMessage(305, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot return book Message is not a String (case 304)");
                                }}));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with return method (case 304)" + e);
                        }
                        break;
                    case (306):
                        /**
                         * do: librarian wants to view subscribers in the system
                         * in: none
                         * return: (id 307) arraylist<subscriber>
                         */
                        try {
                            client.sendToClient(new ClientServerMessage(307, dbController.viewAllSubscribers()));
                            System.out.println("Subscribers list was sent to client");
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscribers list (case 306)" + e);
                            client.sendToClient(new ClientServerMessage(307, null));
                        }
                        break;
                    case (308):
                        /**
                         * do: librarian wants to view details of a specific subscriber
                         * in: Int {subscriber id}
                         * return: (id 309) subscriber
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(309, dbController.viewSubscriberDetails((Integer) message.getMessageContent())));
                                System.out.println("Subscriber details were sent to client");
                            } else {
                                System.out.println("Input is not an instance of Integer (case 308)");
                                client.sendToClient(new ClientServerMessage(309, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscriber details (case 308)" + e);
                            client.sendToClient(new ClientServerMessage(309, null));
                        }
                        break;
                    case (310):
                        /**
                         * do: librarian wants to extend the borrow time for a book that was borrowed by a subscriber
                         * in: ArrayList<String> {subscriber id, book id, return date(yyyy-MM-dd HH:mm:ss), librarian id}
                         * return: (id 311) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(311, dbController.extendBookBorrowTimeLibrarian((ArrayList<String>) message.getMessageContent())));
                                System.out.println("Borrow time was extended by librarian");
                            } else {
                                client.sendToClient(new ClientServerMessage(311, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot extend borrow time Message is not an ArrayList<String>");
                                }}));
                                System.out.println("Cannot extend borrow time Message is not an ArrayList<String> (case 310)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with extend borrow time method (case 310)" + e);
                            client.sendToClient(new ClientServerMessage(311, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                        }
                        break;
                    default:
                        System.out.println("Invalid command id(handleMessageFromClient ServerController)");
                        client.sendToClient(null);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: incorrect msg object type (handleMessageFromClient ServerController)" + e);
        }
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
        // connect to the database (get instance)
        this.dbController = DBController.getInstance();
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    /**
     * This method sends a message to all clients connected to the server.
     *
     * @param message The message to be sent to all clients.
     */
    public void sendMessagesToAllClients(ClientServerMessage message) {
        try {
            sendToAllClients(message);
        } catch (Exception e) {
            System.out.println("Error: sending message to all clients" + e);
        }
    }
}