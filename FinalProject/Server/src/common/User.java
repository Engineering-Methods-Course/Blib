package common;

import java.io.Serializable;

public class User implements Serializable
{
    protected String firstName;
    protected String lastName;

    /**
     * Constructor for the User class.
     *
     * @param firstName
     * @param lastName
     */
    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}