package com.example.weclass.login;
import android.accessibilityservice.GestureDescription;

public class UserItem {

    public String email, fullname;

    public UserItem (){

    }

    public UserItem (String email, String fullname){
        this.email = email;
        this.fullname = fullname;
    }

}
