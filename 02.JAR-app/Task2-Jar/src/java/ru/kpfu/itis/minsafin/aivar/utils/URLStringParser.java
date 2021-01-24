package ru.kpfu.itis.minsafin.aivar.utils;

public class URLStringParser {
    public String[] parse(String url){
        return url.split(";");
    }
}
