package common;

import java.io.Serializable;

public class LibrarianMessage implements Serializable
{
    private String date;
    private String content;

    /**
     * Constructor for the LibrarianMessage class
     *
     * @param date    the date of the message
     * @param content the content of the message
     */
    public LibrarianMessage(String date, String content)
    {
        this.date = date;
        this.content = content;
    }

    /**
     * Returns the date of the message
     *
     * @return the date of the message
     */
    public String getDate()
    {
        return date;
    }
    /**
     * Returns the content of the message
     *
     * @return the content of the message
     */
    public String getContent()
    {
        return content;
    }
}