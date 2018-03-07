package com.coaxys.akta.models;

public class AktaFile {

    public String uid;
    public String project;
    public String arbo;
    public String fileName;
    public int version;
    public AktaUrl urls;

    @Override
    public String toString() {
        return "AktaFile{" +
                "uid='" + uid + '\'' +
                ", project='" + project + '\'' +
                ", arbo='" + arbo + '\'' +
                ", fileName='" + fileName + '\'' +
                ", version='" + version + '\'' +
                ", urls=" + urls +
                '}';
    }
}
