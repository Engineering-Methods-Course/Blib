package common;

import java.util.Date;

public class SubscriberHistory
{
    private Date date;
    private String actionType;
    private String description;

    /**
     * Constructor for the SubscriberHistory class.
     *
     * @param date        The date of the history entry.
     * @param actionType  The type of the history entry.
     * @param description The description of the history entry.
     */
    public SubscriberHistory(Date date, String actionType, String description)
    {
        this.actionType = actionType;
        this.description = description;
        this.date = date;
    }

    /**
     * Gets the date of the history entry.
     *
     * @return The date of the history entry.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Sets the date of the history entry.
     *
     * @param date The date of the history entry.
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Gets the type of the history entry.
     *
     * @return The type of the history entry.
     */
    public String getActionType()
    {
        return actionType;
    }

    /**
     * Sets the type of the history entry.
     *
     * @param actionType The type of the history entry.
     */
    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }

    /**
     * Gets the description of the history entry.
     *
     * @return The description of the history entry.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description of the history entry.
     *
     * @param description The description of the history entry.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}