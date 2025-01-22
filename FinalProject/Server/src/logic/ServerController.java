package logic;


import common.ClientServerMessage;
import common.Librarian;
import common.Subscriber;
import common.User;
import gui.ServerMonitorFrameController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class extends the AbstractServer class and implements the SubscriberController and LibrarianController interfaces.
 * It is used to control the server.
 */
public class ServerController extends AbstractServer {

    private final ServerMonitorFrameController serverMonitorController;
    private final NotificationController notificationController;
    private final ScheduleController scheduleController;
    private DBController dbController;

    /**
     * Constructor to initialize the ServerController object.
     *
     * @param port                    the port number to listen on.
     * @param serverMonitorController the server monitor controller.
     */
    public ServerController(int port, ServerMonitorFrameController serverMonitorController) {
        super(port);
        this.serverMonitorController = serverMonitorController;
        notificationController = NotificationController.getInstance();
        scheduleController = ScheduleController.getInstance();
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
                 * 102 - user logged out
                 * 104 - user closed the app
                 * 200 - subscriber wants to search a book by its name
                 * 202 - subscriber wants to search a book by its genre
                 * 204 - subscriber wants to search a book by its description
                 * 206 - subscriber wants to check book availability of a specific book
                 * 208 - subscriber wants to order a book
                 * 210 - subscriber wants to see his borrowed book list
                 * 212 - subscriber wants to extend his borrow time
                 * 214 - subscriber wants to to view his history
                 * 216 - subscriber wants to edit his personal information
                 * 218 - subscriber wants to edit his login information
                 * 300 - librarian wants to register a new subscriber into the system
                 * 302 - librarian borrows a book to a subscriber
                 * 304 - librarian returns a book of a subscriber to the library
                 * 306 - librarian wants to view subscribers in the system
                 * 308 - librarian wants to view details of a specific subscriber
                 * 310 - librarian wants to extend the borrow time for a book subscriber that was borrowed by a subscriber
                 * 312 - librarian wants to view Borrow Times Logs
                 * 314 - librarian wants to view Subscriber Status Log
                 * 316 - librarian wants to view messages
                 */
                ClientServerMessage message = (ClientServerMessage) msg;
                switch (message.getId()) {
                    case (100):

                        /*
                         * Do: user wants to login
                         * In: arraylist<username,password>
                         * Return:(id 101) subscriber / librarian base of the username
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {

                                User user = dbController.userLogin((ArrayList<String>) message.getMessageContent());

                                if (user instanceof Subscriber) {

                                    /*
                                     * Add the subscriber to the list of subscriber clients
                                     */
                                    notificationController.addSubscriberClients(((Subscriber) user).getID(), client);
                                }
                                if (user instanceof Librarian) {
                                    /*
                                     * Add the librarian to the list of librarian clients
                                     */
                                    notificationController.addLibrarianClients(((Librarian) user).getID(), client);
                                }
                                client.sendToClient(new ClientServerMessage(101, user));
                                System.out.println("user details were sent to client");
                            }

                            /*
                             * If the message is not an ArrayList<String> send a message to the client
                             */
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

                        /*
                         * Do: user logs out
                         * In: user id (librarian or subscriber)
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {

                                /*
                                 * Remove the subscriber or librarian from the list of subscriber clients
                                 */
                                notificationController.removeSubscriberClients((Integer) message.getMessageContent());
                                notificationController.removeLibrarianClients((Integer) message.getMessageContent());
                                System.out.println("User " + message.getMessageContent() + " logged out");
                            } else {
                                System.out.println("Cannot log out - message is not an integer (case 102)");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with user logout method (case 102)" + e);
                        }
                        client.sendToClient(null); // send null to client to make him stop waiting for a response
                        System.out.println("User " + client + " logged out");
                        break;
                    case (104):

                        /*
                         * Do: disconnect client from server
                         */
                        serverMonitorController.clientDisconnected(client);
                        client.sendToClient(null); // send null to client to make him stop waiting for a response
                        System.out.println("Client " + client + " disconnected");
                        break;
                    case (200):

                        /*
                         * Do: subscriber searches a book by its name
                         * In: string
                         * return: (id 201) arraylist<book>
                         */
                        if (message.getMessageContent() instanceof String) {
                            try {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByName((String) message.getMessageContent())));
                                System.out.println("Books search by name was sent to client");
                            } catch (Exception e) {
                                client.sendToClient(new ClientServerMessage(201, null));
                                System.out.println("Error: with getting book search by name (case 200)" + e);

                            }
                        } else {
                            client.sendToClient(new ClientServerMessage(201, null));
                            System.out.println("Cannot search book by name - message is not a string (case 200)");


                        }
                        break;
                    case (202):

                        /*
                         * Do: subscriber searches a book by its genre
                         * In: string
                         * Return: (id 201) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByGenre((String) message.getMessageContent())));
                                System.out.println("Books search by genre was sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(201, null));
                                System.out.println("Cannot search book by genre - message is not a string (case 202)");

                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(201, null));
                            System.out.println("Error: with getting book search by genre (case 202)" + e);
                        }
                        break;
                    case (204):

                        /*
                         * Do: subscriber searches a book by its description
                         * In: string
                         * Return: (id 201) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(201, dbController.searchBookByDescription((String) message.getMessageContent())));
                                System.out.println("Books search by description was sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(201, null));
                                System.out.println("Cannot search book by description - message is not a string (case 204)");

                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(201, null));
                            System.out.println("Error: with getting book search by description (case 204)" + e);

                        }
                        break;
                    case (206):

                        /*
                         * Do: subscriber checks book availability of a specific book
                         * In: int
                         * Return: (id 207) book
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(207, dbController.checkBookAvailability((Integer) message.getMessageContent())));
                                System.out.println("Book details were sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(207, null));
                                System.out.println("Cannot get book details - message is not an integer (case 206)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(207, null));
                            System.out.println("Error: with getting book details (case 206)" + e);
                        }
                        break;
                    case (208):

                        /*
                         * Do: subscriber reserves a book
                         * In: ArrayList<String> {subscriber id, book id}
                         * Return: (id 209) arraylist<string> {success/fail, error message}
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
                            client.sendToClient(new ClientServerMessage(209, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                            System.out.println("Error: with reserve method (case 208)" + e);
                        }
                        break;
                    case (210):

                        /*
                         * Do: subscriber views his borrowed books list
                         * In: int
                         * Return: (id 211) arraylist<book>
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(211, dbController.showSubscriberBorrowedBooks((Integer) message.getMessageContent())));
                                System.out.println("Subscriber borrowed books were sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(211, null));
                                System.out.println("Cannot find borrowed books - message is not an integer (case 210)");

                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(211, null));
                            System.out.println("Error: with getting borrowed book details (case 210)" + e);

                        }
                        break;
                    case (212):

                        /*
                         * Do: subscriber extends a borrowed books borrow time
                         * In: ArrayList<String> {subscriber id, copy id}
                         * Return: (id 213) ArrayList<String> {success/fail, error message}
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

                        /*
                         * Do: subscriber views his history
                         * In: int
                         * Return: (id 215) ArrayList<MonthlyReport> {subscriber history}
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(215, dbController.getSubscriberHistory((Integer) message.getMessageContent())));
                                System.out.println("Subscriber history was sent to client");
                            } else {
                                System.out.println("Cannot get subscriber history - message is not an integer (case 214)");
                                client.sendToClient(new ClientServerMessage(215, null));
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with getting subscriber history (case 214)" + e);
                            client.sendToClient(new ClientServerMessage(215, null));
                        }
                        break;
                    case (216):

                        /*
                         * Do: subscriber edits his personal information
                         * In: ArrayList<String>
                         * Return: (id 217) arraylist<string> {success/fail, error message}
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

                        /*
                         * Do: subscriber edits his login information
                         * In: int
                         * Return: (id 217) arraylist<string> {success/fail, error message}
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
                            client.sendToClient(new ClientServerMessage(219, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 218)");
                            }}));
                            System.out.println("Error: with Edit method (case 218)" + e);
                        }
                        break;
                    case (300):

                        /*
                         * Do: librarian registers a new subscriber into the system
                         * In: ArrayList<String> {subscriber details}
                         * Return: (id 301) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof ArrayList) {
                                client.sendToClient(new ClientServerMessage(301, dbController.registerNewSubscriber((ArrayList<String>) message.getMessageContent())));
                            } else {
                                client.sendToClient(new ClientServerMessage(301, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot register account Message is not an ArrayList<String> (case 300)");
                                }}));
                                System.out.println("Cannot register account Message is not an ArrayList<String> (case 300)");
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Error: with register method (case 300)" + e);
                            client.sendToClient(new ClientServerMessage(301, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error (case 300)");
                            }}));
                        }
                        break;
                    case (302):

                        /*
                         * Do: librarian borrows a book to a subscriber
                         * In: ArrayList<String> {subscriber id, book id, return date}
                         * Return: (id 303) ArrayList<String> {success/fail, error message}
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

                        /*
                         * Do: librarian returns a book of a subscriber to the library
                         * In: String {book id}
                         * Return: (id 305) ArrayList<String> {success/fail, error message}
                         */
                        try {
                            if (message.getMessageContent() instanceof String) {
                                client.sendToClient(new ClientServerMessage(305, dbController.returnBookFromSubscriber((String) message.getMessageContent())));
                                System.out.println("Book was returned from subscriber");
                            } else {
                                client.sendToClient(new ClientServerMessage(305, new ArrayList<String>() {{
                                    add("fail");
                                    add("Cannot return book Message is not a String (case 304)");
                                }}));
                                System.out.println("Cannot return book Message is not a String");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: with return method (case 304)" + e);
                        }
                        break;
                    case (306):

                        /*
                         * Do: librarian views all subscribers in the system
                         * In: none
                         * Return: (id 307) arraylist<subscriber>
                         */
                        try {
                            client.sendToClient(new ClientServerMessage(307, dbController.viewAllSubscribers()));
                            System.out.println("Subscribers list was sent to client");
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(307, null));
                            System.out.println("Error: with getting subscribers list (case 306)" + e);

                        }
                        break;
                    case (308):

                        /*
                         * Do: librarian views the details of a specific subscriber
                         * In: Int {subscriber id}
                         * Return: (id 309) subscriber
                         */
                        try {
                            if (message.getMessageContent() instanceof Integer) {
                                client.sendToClient(new ClientServerMessage(309, dbController.viewSubscriberDetails((Integer) message.getMessageContent())));
                                System.out.println("Subscriber details were sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(309, null));
                                System.out.println("Input is not an instance of Integer (case 308)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(309, null));
                            System.out.println("Error: with getting subscriber details (case 308)" + e);

                        }
                        break;
                    case (310):

                        /*
                         * Do: librarian extends the borrow time for a book that was borrowed by a subscriber
                         * In: ArrayList<String> {subscriber id, book id, return date(yyyy-MM-dd HH:mm:ss), librarian id}
                         * Return: (id 311) ArrayList<String> {success/fail, error message}
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
                            client.sendToClient(new ClientServerMessage(311, new ArrayList<String>() {{
                                add("fail");
                                add("Server Error");
                            }}));
                            System.out.println("Error: with extend borrow time method (case 310)" + e);
                        }
                        break;
                    case (312):

                        /*
                         * Do: librarian views the Borrow Times Log
                         * In: List<java.util.Date> [start date, end date]
                         * Return: (id 313) ArrayList<MonthlyReport> {borrow time logs}
                         */
                        try {
                            if (message.getMessageContent() instanceof List) {
                                client.sendToClient(new ClientServerMessage(313, dbController.exportBorrowTimeLogs((List<Date>) message.getMessageContent())));
                                System.out.println("Borrow Time Logs were sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(313, null));
                                System.out.println("Cannot get Borrow Time Logs - message is not a List<Date> (case 312)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(313, null));
                            System.out.println("Error: with getting Borrow Time Logs (case 312)" + e);
                        }
                        break;
                    case (314):

                        /*
                         * Do: librarian views the Subscriber Status Log
                         * In: List<java.util.Date> [start date, end date]
                         * Return: (id 315) ArrayList<MonthlyReport> {subscriber status logs}
                         */
                        try {
                            if (message.getMessageContent() instanceof List) {
                                client.sendToClient(new ClientServerMessage(315, dbController.exportSubscriberStatusLogs((List<Date>) message.getMessageContent())));
                                System.out.println("Subscriber Status Logs were sent to client");
                            } else {
                                client.sendToClient(new ClientServerMessage(315, null));
                                System.out.println("Cannot get Subscriber Status Logs - message is not a List<Date> (case 314)");
                            }
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(315, null));
                            System.out.println("Error: with getting Subscriber Status Logs (case 314)" + e);
                        }
                        break;
                    case (316):

                        /*
                         * Do: librarian views her messages
                         * Return: (id 317) ArrayList<String> {messages}
                         */
                        try {
                            client.sendToClient(new ClientServerMessage(317, dbController.ViewLibrarianMessages()));
                            System.out.println("Messages were sent to client");
                        } catch (Exception e) {
                            client.sendToClient(new ClientServerMessage(317, null));
                            System.out.println("Error: with getting messages (case 316)" + e);
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
            try {
                client.sendToClient(null);
            } catch (Exception e1) {
                System.out.println("Error: sending null to client" + e1);

            }
        }
    }
}