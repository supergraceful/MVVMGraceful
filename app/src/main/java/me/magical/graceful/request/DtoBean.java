package me.magical.graceful.request;

import java.io.Serializable;


public class DtoBean {

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String from;
    private String name;

    @Override
    public String toString() {
        return '\''+ from + '\'' + name + '\'' ;
    }
}
