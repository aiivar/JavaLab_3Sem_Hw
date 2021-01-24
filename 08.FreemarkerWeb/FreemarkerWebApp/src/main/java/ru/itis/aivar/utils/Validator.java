package ru.itis.aivar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean validatePassword(String password){
        return password.length() >= 8;
    }

    public static boolean validateLogin(String login){
        return login.length() >= 5;
    }

    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(".+@.+\\..+");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

}
