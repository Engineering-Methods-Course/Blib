package common;

import java.io.Serializable;

public class Librarian extends User implements Serializable {
    private  int librarianID;
    private String firstName;
    private String lastName;


    /**
     * °
     * Constructor to initialize a local Librarian object with all necessary details from the server.
     *
     * @param librarianID - the ID of the librarian
     * @param firstName   - the first name of the librarian
     * @param lastName    - the last name of the librarian
     */
    public Librarian(int librarianID, String firstName, String lastName) {
        super(firstName, lastName);
        this.librarianID = librarianID;
    }

    /**
     * Gets the ID of the librarian.
     *
     * @return the ID of the librarian
     */
    public int getLibrarianID() {
        return librarianID;
    }

    /**
     * Gets the first name of the librarian.
     *
     * @return the first name of the librarian
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the librarian.
     *
     * @return the last name of the librarian
     */
    public String getLastName() {
        return lastName;
    }


}
