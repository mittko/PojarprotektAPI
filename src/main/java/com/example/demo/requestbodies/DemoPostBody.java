package com.example.demo.requestbodies;

public class DemoPostBody {

    private int id;
    private String name;

    public DemoPostBody() {}

    public DemoPostBody(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
