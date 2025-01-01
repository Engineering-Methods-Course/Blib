package common;

import java.io.Serializable;

public class Librarian implements Serializable
{
    private int librarianID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    /**
     * Constructor to initialize a local Librarian object with all necessary details from the server.
     *
     * @param librarianID - the ID of the librarian
     * @param firstName   - the first name of the librarian
     * @param lastName    - the last name of the librarian
     */
    public Librarian(int librarianID, String firstName, String lastName, String username, String password)
    {
        this.librarianID = librarianID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
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
     * Gets the username of the librarian.
     *
     * @return the username of the librarian
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Gets the password of the librarian.
     *
     * @return the password of the librarian
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets Username of the librarian.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Sets Password of the librarian.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

}
