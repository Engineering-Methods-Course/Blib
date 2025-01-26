package common;

import java.io.Serializable;

public class User implements Serializable
{
    protected String firstName;
    protected String lastName;

    /**
     * Constructor for the User class.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     */
    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}