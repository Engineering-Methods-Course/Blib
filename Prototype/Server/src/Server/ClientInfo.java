package Server;

public class ClientInfo {
    private byte[] IP;
    private String host;
    private String Status;

    public ClientInfo(byte[] column1, String column2, String column3) {
        this.IP = column1;
        this.host = column2;
        this.Status = column3;
    }

    public byte[] getIP() {
        return IP;
    }

    public String getHost() {
        return host;
    }

    public String getStatus() {
        return Status;
    }

}