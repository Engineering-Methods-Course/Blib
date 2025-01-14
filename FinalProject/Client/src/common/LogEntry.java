package common;

import java.util.Date;

public class LogEntry
{
    private Date date;
    private String type;
    private String description;

    public LogEntry(Date date, String type, String description)
    {
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public String getType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }
}
