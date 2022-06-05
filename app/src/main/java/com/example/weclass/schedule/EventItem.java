package com.example.weclass.schedule;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventItem
{

    private int id;
    private String name;
    private String date;
    private String time;

    public EventItem(int id , String name,  String time, String date)
    {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;

    }


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}
