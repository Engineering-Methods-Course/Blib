package common;

import java.io.Serializable;

public class Book implements Serializable
{
    private int bookSerialNumber;
    private String bookName;
    private String bookGenre;
    private String bookDescription;
    private int bookCopyNum;
    private int reservedCopyNum;
    private int borrowedCopyNum;

    /**
     * Constructor to initialize a local Book object with all necessary details from the server.
     *
     * @param bookSerialNumber - the serial number of the book
     * @param bookName         - the name of the book
     * @param bookGenre        - the genre of the book
     * @param bookDescription  - the description of the book
     * @param bookCopyNum      - the number of copies of the book
     * @param reservedCopyNum  - the number of reserved copies of the book
     * @param borrowedCopyNum  - the number of borrowed copies of the book
     */
    public Book(int bookSerialNumber, String bookName, String bookGenre, String bookDescription, int bookCopyNum, int reservedCopyNum, int borrowedCopyNum)
    {
        this.bookSerialNumber = bookSerialNumber;
        this.bookName = bookName;
        this.bookGenre = bookGenre;
        this.bookDescription = bookDescription;
        this.bookCopyNum = bookCopyNum;
        this.reservedCopyNum = reservedCopyNum;
        this.borrowedCopyNum = borrowedCopyNum;
    }

    /**
     * Gets the serial number of the book.
     *
     * @return the serial number of the book
     */
    public int getBookSerialNumber()
    {
        return bookSerialNumber;
    }

    /**
     * Gets the name of the book.
     *
     * @return the name of the book
     */
    public String getBookName()
    {
        return bookName;
    }

    /**
     * Gets the genre of the book.
     *
     * @return the genre of the book
     */
    public String getBookGenre()
    {
        return bookGenre;
    }

    /**
     * Gets the description of the book.
     *
     * @return the description of the book
     */
    public String getBookDescription()
    {
        return bookDescription;
    }

    /**
     * Gets the number of copies of the book.
     *
     * @return the number of copies of the book
     */
    public int getBookCopyNum()
    {
        return bookCopyNum;
    }

    /**
     * Gets the number of reserved copies of the book.
     *
     * @return the number of reserved copies of the book
     */
    public int getReservedCopyNum()
    {
        return reservedCopyNum;
    }

    /**
     * Gets the number of borrowed copies of the book.
     *
     * @return the number of borrowed copies of the book
     */
    public int getBorrowedCopyNum()
    {
        return borrowedCopyNum;
    }
}
