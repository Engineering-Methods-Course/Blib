

package client;

import common.ClientServerMessage;
import common.Subscriber;
import gui.EditProfileController;
import gui.LoginController;
import ocsf.client.*;
import common.ChatIF;


import java.io.*;
import java.sql.Connection;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 */
public class ClientController extends AbstractClient
{
    //Instance variables **********************************************
    private Connection conn;
    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;
    public static boolean awaitResponse = false;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param host The server to connect to.
     * @param port The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ClientController(String host, int port, ChatIF clientUI)
            throws IOException
    {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }


    //Instance methods ************************************************

    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg)
    {
        awaitResponse=false; // @@@@@@@@@@ check this if necessary
        try
        {
            if(msg instanceof ClientServerMessage)
            {
                ClientServerMessage message = (ClientServerMessage) msg;

                /**
                 * 202 - Get info of a specific subscriber back from the server
                 * 204 - Edit info of a specific subscriber
                 * 206 - Get a list of all subscribers
                 */
                switch (message.getId()){
                    // get the details of i
                    case 202:
                        if(message.getMessageContent()==null){
                            System.out.println("Wrong Username(id) or Password");
                        }
                        else if(message.getMessageContent() instanceof Subscriber)
                        {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            LoginController.setLocalSubscriber(subscriberFromServer);
                        }
                        return;
                    //  Edit subscriber details
                    case 204:
                        if(message.getMessageContent()==null){
                            System.out.println("Could not update");
                        }
                        else if(message.getMessageContent() instanceof Subscriber)
                        {
                            Subscriber subscriberFromServer = (Subscriber) message.getMessageContent();
                            LoginController.setLocalSubscriber(subscriberFromServer);
                            EditProfileController.setLocalSubscriber(subscriberFromServer);
                        }
                        return;
                    default:
                        System.out.println("Invalid command id");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Error: incorrect msg object type" + e);
        }
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */

    public void handleMessageFromClientUI(ClientServerMessage message) {
        try {
            openConnection(); // Ensure the connection is open before sending
            awaitResponse = true; // Mark as waiting for a response
            sendToServer(message); // Send the ClientServerMessage object
            // Wait for the server response
            while (awaitResponse) {
                try {
                    Thread.sleep(100); // Poll for the response
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client. " + e);
            quit();
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit()
    {
        try
        {
            closeConnection();
        }
        catch(IOException e) {}
        System.exit(0);
    }
}
//End of ClientController class
