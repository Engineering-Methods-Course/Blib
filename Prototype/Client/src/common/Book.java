package common;

import java.io.Serializable;

public class Book implements Serializable
{
    private String bookName;
    private String bookGenre;
    private String bookDescription;
    private String bookLocation;
    private int bookAvailableCopyNum;

    /**
     * Constructor to initialize a local Book object with all necessary details from the server.
     *
     * @param bookName             - the name of the book
     * @param bookGenre            - the genre of the book
     * @param bookDescription      - the description of the book
     * @param bookLocation         - the location of the shelf the book is on
     * @param bookAvailableCopyNum - the number of available copies of the book
     */
    public Book(String bookName, String bookGenre, String bookDescription, String bookLocation, int bookAvailableCopyNum)
    {
        this.bookName = bookName;
        this.bookGenre = bookGenre;
        this.bookDescription = bookDescription;
        this.bookLocation = bookLocation;
        this.bookAvailableCopyNum = bookAvailableCopyNum;
    }

    public String getBookName()
    {
        return bookName;
    }

    public String getBookGenre()
    {
        return bookGenre;
    }

    public String getBookDescription()
    {
        return bookDescription;
    }

    public String getBookLocation()
    {
        return bookLocation;
    }

    public int getBookAvailableCopyNum()
    {
        return bookAvailableCopyNum;
    }
}
