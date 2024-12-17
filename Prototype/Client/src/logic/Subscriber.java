package logic;

public class Subscriber {

    // Unique identifier for the subscriber
    private String id;

    // First name of the subscriber
    private String name;

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

    // Subscriber's account password
    private String password;

    /**
     * Constructor to initialize a Subscriber object with all necessary details.
     *
     * @param id Unique identifier for the subscriber
     * @param name First name of the subscriber
     * @param lastName Last name of the subscriber
     * @param subscriptionHistory Number of subscriptions the subscriber has had
     * @param phoneNumber Subscriber's phone number
     * @param email Subscriber's email address
     * @param password Subscriber's account password
     */
    public Subscriber(String id, String name, String lastName, int subscriptionHistory, String phoneNumber, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.subscriptionHistory = subscriptionHistory;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the unique identifier of the subscriber.
     *
     * @return the id of the subscriber
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the unique identifier of the subscriber.
     *
     * @param id Unique identifier for the subscriber
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the first name of the subscriber.
     *
     * @return the name of the subscriber
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name of the subscriber.
     *
     * @param name First name of the subscriber
     */
    public void setName(String name) {
        this.name = name;
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
     * Sets the subscription history of the subscriber.
     *
     * @param subscriptionHistory Number of subscriptions the subscriber has had
     */
    public void setSubscriptionHistory(int subscriptionHistory) {
        this.subscriptionHistory = subscriptionHistory;
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
    public boolean statusIsFrozen() {
        return statusIsFrozen;
    }

    /**
     * Sets the frozen status of the subscriber's account.
     *
     * @param statusIsFrozer true if the account should be frozen, false otherwise
     */
    public void setStatusIsFrozer(boolean statusIsFrozer) {
        this.statusIsFrozen = statusIsFrozer;
    }

    /**
     * Gets the password of the subscriber.
     *
     * @return the password of the subscriber
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the subscriber.
     *
     * @param password Subscriber's account password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
