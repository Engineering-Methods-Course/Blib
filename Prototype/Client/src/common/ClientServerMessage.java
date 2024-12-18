package common;

public class ClientServerMessage {
    private String id;
    private Object messageContent;

    public ClientServerMessage(String id, Object messageContent) {
        this.id = id;
        this.messageContent = messageContent;
    }
    public String getId() {
        return id;
    }
    public Object getMessageContent() {
        return messageContent;
    }
}
