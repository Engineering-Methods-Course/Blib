package common;

import java.io.Serializable;

public class BorrowedBook implements Serializable
{
    private int bookSerialNumber;
    private int copyID;
    private String borrowerID;
    private String borrowDate;
    private String expectedReturnDate;
    private String returnDate;

    /**
     * Constructor to initialize a local BorrowedBook object with all necessary details from the server.
     *
     * @param bookSerialNumber - the serial number of the book
     * @param copyID           - the ID of the copy
     * @param borrowerID       - the ID of the borrower
     * @param borrowDate       - the date the book was borrowed
     * @param expectedReturnDate       - the date the book is to be returned
     */
    public BorrowedBook(int bookSerialNumber, int copyID, String borrowerID, String borrowDate, String expectedReturnDate, String returnDate)
    {
        this.bookSerialNumber = bookSerialNumber;
        this.copyID = copyID;
        this.borrowerID = borrowerID;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnDate = returnDate;
    }
    {
        this.bookSerialNumber = bookSerialNumber;
        this.copyID = copyID;
        this.borrowerID = borrowerID;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
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
    public String getBorrowerID()
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
    public String getReturnDate(){
        return returnDate;
    }
}
