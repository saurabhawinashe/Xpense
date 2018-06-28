package com.homex.fexp;

/**
 * Created by saurabh on 26/9/17.
 */

public class Mydata {
    String email,password,name,phone_number;
    int budget, hour, minute;
    public Mydata(){}
    public Mydata(String email, String password,String name, String phone_number, int budget, int hour, int minute)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
        this.budget = budget;
        this.hour = hour;
        this.minute = minute;
    }
    public String getEmail() { return this.email;}
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getPhone_number()
    {
        return this.phone_number;
    }
    public void setPhone_number(String phone_number)
    {
        this.phone_number=phone_number;
    }
    public int getBudget(){
        return this.budget;
    }
    public void setBudget(int budget){
        this.budget = budget;
    }
    public int getHour(){
        return this.hour;
    }
    public void setHour(int hour){
        this.hour = hour;
    }
    public int getMinute(){
        return this.minute;
    }
    public void setMinute(int minute){
        this.minute = minute;
    }
}