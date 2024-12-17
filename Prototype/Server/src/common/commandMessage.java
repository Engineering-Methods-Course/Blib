package common;

public class commandMessage {
    private int id;
    private Object messageContent;

    public commandMessage(int id, Object messageContent) {
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
