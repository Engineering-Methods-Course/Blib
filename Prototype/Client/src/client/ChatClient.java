package client;

import common.ChatIF;
import common.ClientServerMessage;

import java.io.IOException;


public class ChatClient implements ChatIF {

    ClientController client;

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ChatClient(String host, int port) throws Exception {
        try {
            client = new ClientController(host, port, this);
        } catch (IOException exception) {

            System.out.println("Error: Can't setup connection!" + " Terminating client.");
            throw exception;
            //System.exit(1);
        }
    }

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the client's message handler.
     *
     * @param msg The message from the console.
     */
    public void accept(ClientServerMessage msg) {
        client.handleMessageFromClientUI(msg);
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {
        System.out.println("> " + message);
    }
}
