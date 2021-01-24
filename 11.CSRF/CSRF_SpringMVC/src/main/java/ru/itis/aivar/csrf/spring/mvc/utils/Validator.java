package ru.itis.aivar.csrf.spring.mvc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public boolean validatePassword(String password){
        return password.length() >= 8;
    }

    public boolean validateLogin(String login){
        return login.length() >= 5;
    }

    public boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(".+@.+\\..+");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

}
