package ru.itis.aivar.utils;

public class URLStringParser {
    public String[] parse(String url){
        return url.split(";");
    }
}
