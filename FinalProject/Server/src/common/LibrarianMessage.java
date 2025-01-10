package common;

import java.io.Serializable;

public class LibrarianMessage implements Serializable
{
    private String date;
    private String content;

    public LibrarianMessage(String date, String content)
    {
        this.date = date;
        this.content = content;
    }

    public String getDate()
    {
        return date;
    }

    public String getContent()
    {
        return content;
    }
}