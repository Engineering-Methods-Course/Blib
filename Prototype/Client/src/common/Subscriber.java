package common;

import java.io.Serializable;


public class Subscriber extends User implements Serializable {

    private static Subscriber localSubscriber = null;
    // Unique identifier for the subscriber
    private int id;

    // First name of the subscriber
    private String firstname;

    // Last name of the subscriber
    private String lastName;

    // Subscription history (e.g., the number of subscriptions)
    private int subscriptionHistory;

    // Subscriber's phone number
    private String phoneNumber;

    // Subscriber's email address
    private String email;

    // Status to check if the subscriber is frozen (inactive)
    private boolean statusIsFrozen = false;


    public static Subscriber getLocalSubscriber() {
        return localSubscriber;
    }

    public static void setLocalSubscriber(Subscriber localSubscriber) {
        Subscriber.localSubscriber = localSubscriber;
    }

    /**
     * Constructor to initialize a Subscriber object with all necessary details.
     *
     * @param id          Unique identifier for the subscriber
     * @param firstName   First name of the subscriber
     * @param lastName    Last name of the subscriber
     * @param phoneNumber Subscriber's phone number
     * @param email       Subscriber's email address
     */
    public Subscriber(int id, String firstName, String lastName, String phoneNumber, String email, boolean status, int subscriptionHistory) {
        super(firstName, lastName);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.statusIsFrozen = status;
        this.subscriptionHistory = subscriptionHistory;
    }

    /**
     * Gets the unique identifier of the subscriber.
     *
     * @return the id of the subscriber
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the unique identifier of the subscriber.
     *
     * @param id Unique identifier for the subscriber
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first name of the subscriber.
     *
     * @return the name of the subscriber
     */
    public String getFirstName() {
        return firstname;
    }

    /**
     * Sets the first name of the subscriber.
     *
     * @param name First name of the subscriber
     */
    public void setFirstName(String name) {
        this.firstname = name;
    }

    /**
     * Gets the last name of the subscriber.
     *
     * @return the last name of the subscriber
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the subscriber.
     *
     * @param lastName Last name of the subscriber
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the subscription history of the subscriber.
     *
     * @return the subscription history count
     */
    public int getSubscriptionHistory() {
        return subscriptionHistory;
    }

    /**
     * Gets the phone number of the subscriber.
     *
     * @return the phone number of the subscriber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the subscriber.
     *
     * @param phoneNumber Subscriber's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the subscriber.
     *
     * @return the email address of the subscriber
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the subscriber.
     *
     * @param email Subscriber's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks whether the subscriber's account is frozen.
     *
     * @return true if the account is frozen, false otherwise
     */
    public boolean getStatusIsFrozen() {
        return statusIsFrozen;
    }


    /**
     * Sets the frozen status of the subscriber's account.
     *
     * @param statusIsFrozen true if the account should be frozen, false otherwise
     */
    public void setStatusIsFrozen(boolean statusIsFrozen) {
        this.statusIsFrozen = statusIsFrozen;
    }

}
