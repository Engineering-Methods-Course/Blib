package common;

import java.io.Serializable;

public class User implements Serializable {
    private  String firstName;
    private  String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
