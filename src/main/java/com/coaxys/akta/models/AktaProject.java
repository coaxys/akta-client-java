package com.coaxys.akta.models;

public class AktaProject {

    public String name;

    public String slug;

    public long spaceUsed;

    @Override
    public String toString() {
        return "AktaProject{" +
                "name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", spaceUsed=" + spaceUsed +
                '}';
    }
}
