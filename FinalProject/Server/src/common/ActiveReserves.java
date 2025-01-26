package common;

import java.io.Serializable;
import java.util.Date;

public class ActiveReserves implements Serializable {

    private int serialNumber;
    private String bookName;
    private Date reserveDate;

    /**
     * Constructor for ActiveReserves
     * @param serialNumber the copy ID of the book
     * @param bookName the name of the book
     * @param reserveDate the date the book was reserved
     */
    public ActiveReserves(int serialNumber, String bookName, Date reserveDate) {
        this.bookName = bookName;
        this.reserveDate = reserveDate;
        this.serialNumber = serialNumber;
    }

    /**
     * Get the name of the book
     * @return the name of the book
     */
    public String getBookName() {
        return bookName;
    }

    /**
     * Get the date the book was reserved
     * @return the date the book was reserved
     */
    public Date getReserveDate() {
        return reserveDate;
    }

    /**
     * Get the copy ID of the book
     * @return the copy ID of the book
     */
    public int getSerialNumber() {
        return serialNumber;
    }
}
