package com.zhstar.bountyhelper;

import java.util.Objects;

public class Place {
    private String name;
    private String kill;
    private Integer bogeyNum;

    Place(String name, String kill, Integer bogeyNum) {
        this.name = name;
        this.kill = kill;
        this.bogeyNum = bogeyNum;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Integer getBogeyNum() {
        return bogeyNum;
    }

    public void setBogeyNum(Integer bogeyNum) {
        this.bogeyNum = bogeyNum;
    }

    String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return name.equals(place.name) &&
                kill.equals(place.kill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kill);
    }

    @Override
    public String toString() {
        return "[" + name + ":" + kill + ":" + bogeyNum + "]";
    }
}
