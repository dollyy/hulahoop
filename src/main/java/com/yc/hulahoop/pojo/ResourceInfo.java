package com.yc.hulahoop.pojo;

public class ResourceInfo {
    private Integer id;

    private String name;

    private String path;

    private Integer status;

    public ResourceInfo(Integer id, String name, String path, Integer status) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.status = status;
    }

    public ResourceInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}