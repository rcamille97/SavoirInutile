package com.link.sergio.myapplication;

import org.json.JSONObject;

public class SavoirItem
{
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";

    String title;
    String description;
    String date;

    public SavoirItem(JSONObject o){
        title = o.optString(TITLE);
        description = o.optString(DESCRIPTION);
        date = o.optString(DATE);
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDate()
    {
        return date;
    }
}
