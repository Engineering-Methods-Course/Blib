package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subscriber extends User implements Serializable
{

    private static Subscriber localSubscriber = null;
    private static Subscriber watchProfileSubscriber = null;
    // Unique identifier for the subscriber
    private int id;

    // Subscription history (e.g., the number of subscriptions)
    private final List<ArrayList<String>> subscriptionHistory;

    // Subscriber's phone number
    private String phoneNumber;

    // Subscriber's email address
    private String email;

    // Status to check if the subscriber is frozen (inactive)
    private String statusIsFrozen;

    /**
     * Constructor to initialize a Subscriber object with all necessary details.
     *
     * @param id          Unique identifier for the subscriber
     * @param firstName   First name of the subscriber
     * @param lastName    Last name of the subscriber
     * @param phoneNumber Subscriber's phone number
     * @param email       Subscriber's email address
     * @param status      Status to check if the subscriber is frozen (inactive)
     * @param subscriptionHistory Subscription history (e.g., the number of subscriptions)
     */
    public Subscriber(int id, String firstName, String lastName, String phoneNumber, String email, boolean status, List<ArrayList<String>> subscriptionHistory)
    {
        super(firstName, lastName);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.statusIsFrozen = status ? "Frozen" : "Active";
        this.subscriptionHistory = subscriptionHistory;
    }

    /**
     * Gets the local subscriber object.
     *
     * @return the local subscriber object
     */
    public static Subscriber getLocalSubscriber()
    {
        return localSubscriber;
    }

    /**
     * Gets the watch profile subscriber object.
     *
     * @return the watch profile subscriber object
     */
    public static Subscriber getWatchProfileSubscriber()
    {
        return watchProfileSubscriber;
    }

    /**
     * Sets the watch profile subscriber object.
     *
     * @param watchProfileSubscriber the watch profile subscriber object
     */
    public static void setWatchProfileSubscriber(Subscriber watchProfileSubscriber)
    {
        Subscriber.watchProfileSubscriber = watchProfileSubscriber;
    }

    /**
     * Sets the local subscriber object.
     *
     * @param localSubscriber the local subscriber object
     */
    public static void setLocalSubscriber(Subscriber localSubscriber)
    {
        Subscriber.localSubscriber = localSubscriber;
    }

    /**
     * Gets the unique identifier of the subscriber.
     *
     * @return the id of the subscriber
     */
    public int getID()
    {
        return id;
    }

    /**
     * Sets the unique identifier of the subscriber.
     *
     * @param id Unique identifier for the subscriber
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets the first name of the subscriber.
     *
     * @return the name of the subscriber
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the first name of the subscriber.
     *
     * @param name First name of the subscriber
     */
    public void setFirstName(String name)
    {
        this.firstName = name;
    }

    /**
     * Gets the last name of the subscriber.
     *
     * @return the last name of the subscriber
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the last name of the subscriber.
     *
     * @param lastName Last name of the subscriber
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets the subscription history of the subscriber.
     *
     * @return the subscription history count
     */
    public List<ArrayList<String>> getSubscriptionHistory()
    {
        return subscriptionHistory;
    }

    /**
     * Gets the phone number of the subscriber.
     *
     * @return the phone number of the subscriber
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the subscriber.
     *
     * @param phoneNumber Subscriber's phone number
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the subscriber.
     *
     * @return the email address of the subscriber
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the email address of the subscriber.
     *
     * @param email Subscriber's email address
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Checks whether the subscriber's account is frozen.
     *
     * @return true if the account is frozen, false otherwise
     */
    public String getStatusIsFrozen()
    {
        return statusIsFrozen;
    }

    /**
     * Sets the frozen status of the subscriber's account.
     *
     * @param statusIsFrozen true if the account should be frozen, false otherwise
     */
    public void setStatusIsFrozen(boolean statusIsFrozen)
    {
        this.statusIsFrozen = statusIsFrozen ? "Frozen" : "Active";
    }
}