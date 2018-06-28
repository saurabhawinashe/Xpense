package com.homex.fexp;

public class AddExpense {
    String category, description, date, email;
    int amount;
    public AddExpense(){}
    public AddExpense(String email, String category, String description, int amount, String date)
    {
        this.category = category;
        this.description = description;
        this.date = date;
        this.email = email;
        this.amount = amount;
    }
    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public int getAmount(){
        return this.amount;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
}
