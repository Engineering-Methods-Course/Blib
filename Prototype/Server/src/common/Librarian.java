package common;

import java.io.Serializable;

public class Librarian implements Serializable
{
    private int librarianID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    /**
     * Constructor to initialize a local Librarian object with all necessary details from the server.
     *
     * @param librarianID - the ID of the librarian
     * @param firstName   - the first name of the librarian
     * @param lastName    - the last name of the librarian
     * @param email       - the email of the librarian
     * @param phoneNumber - the phone number of the librarian
     */
    public Librarian(int librarianID, String firstName, String lastName, String email, String phoneNumber)
    {
        this.librarianID = librarianID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the ID of the librarian.
     *
     * @return the ID of the librarian
     */
    public int getLibrarianID()
    {
        return librarianID;
    }

    /**
     * Gets the first name of the librarian.
     *
     * @return the first name of the librarian
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Gets the last name of the librarian.
     *
     * @return the last name of the librarian
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets the email of the librarian.
     *
     * @return the email of the librarian
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Gets the phone number of the librarian.
     *
     * @return the phone number of the librarian
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
}
