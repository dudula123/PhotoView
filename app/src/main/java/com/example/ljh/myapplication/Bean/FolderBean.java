package com.example.ljh.myapplication.Bean;

public class FolderBean {
    private String dir;
    private String firstImgDir;
    private String name;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex =this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndex);
    }

    public String getName() {
        return name;
    }

    public String getFirstImgDir() {
        return firstImgDir;
    }

    public void setFirstImgDir(String firstImgDir) {
        this.firstImgDir = firstImgDir;
    }
}
