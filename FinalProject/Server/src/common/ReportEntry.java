package common;

import java.io.Serializable;
import java.util.Date;

public class ReportEntry implements Serializable
{
    private Date date;
    private String type;
    // Book name or user id
    private String description;

    /**
     * Constructor for the ReportEntry class.
     *
     * @param date        The date of the report entry.
     * @param type        The type of the report entry.
     * @param description The description of the report entry.
     */
    public ReportEntry(Date date, String type, String description)
    {
        this.date = date;
        this.type = type;
        this.description = description;
    }

    /**
     * Gets the date of the report entry.
     *
     * @return The date of the report entry.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Gets the type of the report entry.
     *
     * @return The type of the report entry.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Gets the description of the report entry.
     *
     * @return The description of the report entry.
     */
    public String getDescription()
    {
        return description;
    }
}