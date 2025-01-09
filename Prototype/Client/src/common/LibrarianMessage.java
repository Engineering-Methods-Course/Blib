package common;

import java.io.Serializable;

public class LibrarianMessage implements Serializable
{
    private String date;
    private String content;

    /**
     * Constructor for the LibrarianMessage class.
     *
     * @param date    The date of the message.
     * @param content The content of the message.
     */
    public LibrarianMessage(String date, String content)
    {
        this.date = date;
        this.content = content;
    }

    /**
     * Gets the date of the message.
     *
     * @return The date of the message.
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getContent()
    {
        return content;
    }
}