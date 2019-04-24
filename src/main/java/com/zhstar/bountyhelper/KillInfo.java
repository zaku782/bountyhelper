package com.zhstar.bountyhelper;

import java.util.Map;

public class KillInfo {
    private Map<String, Integer> killBogey;
    private Map<String, Integer> mwyhLimit;
    private Integer levelLimit;

    Map<String, Integer> getKillBogey() {
        return killBogey;
    }

    public void setKillBogey(Map<String, Integer> killBogey) {
        this.killBogey = killBogey;
    }

    Map<String, Integer> getMwyhLimit() {
        return mwyhLimit;
    }

    public void setMwyhLimit(Map<String, Integer> mwyhLimit) {
        this.mwyhLimit = mwyhLimit;
    }

    Integer getLevelLimit() {
        return levelLimit;
    }

    public void setLevelLimit(Integer levelLimit) {
        this.levelLimit = levelLimit;
    }
}
