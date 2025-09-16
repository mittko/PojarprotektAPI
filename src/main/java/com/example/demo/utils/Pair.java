package com.example.demo.utils;

import java.io.File;

public class Pair implements Comparable<Pair>{

    public long t;
    public File f;

    public Pair(File file) {
        this.f = file;
        this.t = file.lastModified();
    }


    @Override
    public int compareTo(Pair o) {
        long u = o.t;
        return -1 * Long.compare(t, u);//t < u ? -1 : t == u ? 0 : 1;
    }
}
