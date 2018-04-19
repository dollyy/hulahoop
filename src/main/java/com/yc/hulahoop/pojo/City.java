package com.yc.hulahoop.pojo;

public class City {
    private Integer id;

    private String name;

    private Integer areaId;

    public City(Integer id, String name, Integer areaId) {
        this.id = id;
        this.name = name;
        this.areaId = areaId;
    }

    public City() {
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}