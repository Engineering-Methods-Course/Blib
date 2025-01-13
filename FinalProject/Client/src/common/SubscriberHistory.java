package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubscriberHistory
{
    private Date date;
    private String actionType;
    private String description;

    public SubscriberHistory(Date date, String actionType, String description)
    {
        this.actionType = actionType;
        this.description = description;
        this.date = date;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getActionType()
    {
        return actionType;
    }

    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
