package common;


import java.io.Serializable;

public class ClientServerMessage implements Serializable {
    private int id;
    private Object messageContent;

    public ClientServerMessage(int id, Object messageContent) {
        this.id = id;
        this.messageContent = messageContent;
    }
    public int getId() {
        return id;
    }
    public Object getMessageContent() {
        return messageContent;
    }
}
