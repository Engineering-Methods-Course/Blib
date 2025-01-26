package common;

import java.io.Serializable;

public class BorrowedBook implements Serializable
{
    private int copyID;
    private int borrowerID;
    private String bookName;
    private String borrowDate;
    private String expectedReturnDate;
    private String returnDate;

    /**
     * Constructor to initialize a local BorrowedBook object with all necessary details from the server.
     *
     * @param copyID             - the ID of the copy
     * @param borrowerID         - the ID of the borrower
     * @param bookName           - the name of the book
     * @param borrowDate         - the date the book was borrowed
     * @param expectedReturnDate - the date the book is to be returned
     * @param returnDate         - the date the book was returned
     */
    public BorrowedBook(int copyID, int borrowerID, String bookName, String borrowDate, String expectedReturnDate, String returnDate)
    {
        this.copyID = copyID;
        this.bookName = bookName;
        this.borrowerID = borrowerID;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
    }

    /**
     * Gets the ID of the copy.
     *
     * @return the ID of the copy
     */
    public int getCopyID()
    {
        return copyID;
    }

    /**
     * Gets the ID of the borrower.
     *
     * @return the ID of the borrower
     */
    public int getBorrowerID()
    {
        return borrowerID;
    }

    /**
     * Gets the date the book was borrowed.
     *
     * @return the date the book was borrowed
     */
    public String getBorrowDate()
    {
        return borrowDate;
    }

    /**
     * Gets the date the book is to be expected returned date.
     *
     * @return the date the book is to be returned
     */
    public String getExpectedReturnDate()
    {
        return expectedReturnDate;
    }

    /**
     * Gets the date the book was returned.
     *
     * @return the date the book was returned
     */
    public String getReturnDate()
    {
        return returnDate;
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
     * Sets the name of the book.
     *
     * @param bookName the name of the book
     */
    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    /**
     * Sets the Expected Return Date of the copy.
     *
     * @param expectedReturnDate the Expected Return Date of the copy
     */
    public void setExpectedReturnDate(String expectedReturnDate)
    {
        this.expectedReturnDate = expectedReturnDate;
    }

    /**
     * Sets the Borrow Date of the copy.
     *
     * @param borrowDate the Borrow Date of the copy
     */
    public void setBorrowDate(String borrowDate)
    {
        this.borrowDate = borrowDate;
    }
}