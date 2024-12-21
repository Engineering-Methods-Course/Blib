
package client;
import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;

import common.ChatIF;
import common.ClientServerMessage;
import common.Subscriber;


/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 */
public class ClientController implements ChatIF
{
    //Class variables *************************************************
    //private Connection conn;
    /**
     * The default port to connect on.
     */
    public static int DEFAULT_PORT ;

    //Instance variables **********************************************

    /**
     * The instance of the client that created this ConsoleChat.
     */
    ChatClient client;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ClientController(String host, int port) throws Exception
    {
        try
        {
            client= new ChatClient(host, port, this);
        }
        catch(IOException exception)
        {

            System.out.println("Error: Can't setup connection!"+ " Terminating client.");
            throw exception;
            //System.exit(1);
        }
    }

    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the client's message handler.
     */
    public void accept(ClientServerMessage msg)
    {
        client.handleMessageFromClientUI(msg);
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message)
    {
        System.out.println("> " + message);
    }
}
//End of ConsoleChat class
