package common;


import java.io.Serializable;

public class ClientServerMessage implements Serializable
{
    //Instance variables
    private final int id;
    private final Object messageContent;

    /**
     * Constructor to initialize a ClientServerMessage object with all necessary details.
     *
     * @param id             Unique identifier for the message
     * @param messageContent The content of the message
     */
    public ClientServerMessage(int id, Object messageContent)
    {
        this.id = id;
        this.messageContent = messageContent;
    }

    /**
     * Gets the unique identifier of the message.
     *
     * @return the id of the message
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the content of the message.
     *
     * @return the content of the message
     */
    public Object getMessageContent()
    {
        return messageContent;
    }
}
