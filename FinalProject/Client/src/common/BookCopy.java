package common;

public class BookCopy {
    private int bookSerialNumber;
    private int copyID;
    private String copyAvailability;
    private String shelfLocation;

    /**
     * Constructor to initialize a local BookCopy object with all necessary details from the server.
     *
     * @param bookSerialNumber - the serial number of the book
     * @param copyID           - the serial number of the copy
     * @param copyAvailability - the status of the copy (available, reserved, borrowed, lost)
     * @param shelfLocation    - the location of the copy on the shelf
     */
    public BookCopy(int bookSerialNumber, int copyID, String copyAvailability, String shelfLocation)
    {
        this.bookSerialNumber = bookSerialNumber;
        this.copyID = copyID;
        this.copyAvailability = copyAvailability;
        this.shelfLocation = shelfLocation;
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
     * Gets the serial number of the copy.
     *
     * @return the serial number of the copy
     */
    public int getCopySerialNumber()
    {
        return copyID;
    }

    /**
     * Gets the status of the copy.
     *
     * @return the status of the copy
     */
    public String getCopyStatus()
    {
        return copyAvailability;
    }
    /**
     * Gets the location of the copy on the shelf.
     *
     * @return the location of the copy on the shelf
     */
    public String getShelfLocation(){
        return shelfLocation;
    }
}
