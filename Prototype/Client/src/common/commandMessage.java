package common;

public class commandMessage {
    private String id;
    private Object messageContent;

    public commandMessage(String id, Object messageContent) {
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
