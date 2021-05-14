package ru.itis.aivar.em.queries;

import java.util.ArrayList;
import java.util.List;

public class Expression {

    private List<Where> where;
    private List<Value> values;
    private boolean isEmpty;

    public Expression() {
        this.where = new ArrayList<>();
        this.values = new ArrayList<>();
        this.isEmpty = true;
    }

    public static Expression create(){
        return new Expression();
    }

    public Expression addWhere(String key, String value, String separator, boolean isText){
        if (isEmpty) isEmpty = false;
        where.add(new Where(key, value, separator, isText));
        return this;
    }

    public Expression addWhere(String key, String value, String separator){
        if (isEmpty) isEmpty = false;
        where.add(new Where(key, value, separator, true));
        return this;
    }

    public Expression addValues(String key, String value, boolean isText){
        if (isEmpty) isEmpty = false;
        values.add(new Value(key, value, isText));
        return this;
    }

    public Expression addValues(String key, String value){
        if (isEmpty) isEmpty = false;
        values.add(new Value(key, value, true));
        return this;
    }

    public void setWhere(List<Where> where) {
        this.where = where;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public List<Where> getWhere() {
        return where;
    }

    public List<Value> getValues() {
        return values;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    class Where{
        private String key;
        private String value;
        private String separator;
        private boolean isText;

        public Where(String key, String value, String separator, boolean isText) {
            this.key = key;
            this.value = value;
            this.separator = separator;
            this.isText = isText;
        }

        public boolean isText() {
            return isText;
        }

        public void setText(boolean text) {
            isText = text;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getSeparator() {
            return separator;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }
    }
    class Value{
        private String key;
        private String value;
        private boolean isText;

        public Value(String key, String value, boolean isText) {
            this.key = key;
            this.value = value;
            this.isText = isText;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public boolean isText() {
            return isText;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setText(boolean text) {
            isText = text;
        }
    }
}
