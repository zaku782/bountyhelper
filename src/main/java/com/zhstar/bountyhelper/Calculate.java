package com.zhstar.bountyhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Calculate {

    @Autowired
    Data data;

    private Map<String, Integer> priority = new HashMap<>() {
        {
            put("故事", 3);
            put("御魂", 2);
            put("秘闻", 1);
        }
    };

    public void doCal(Map<String, Integer> res, KillInfo killInfo) {

        //将列表里面使用别名的妖怪转换成对应的妖怪
        List<String> waitRemove = new ArrayList<>();
        Map<String, Integer> waitAdd = new HashMap<>();
        for (String name : killInfo.getKillBogey().keySet()) {
            String realName = data.getPuzzleMap().get(name);
            if (realName != null) {
                //如果这是个别名
                //列表中已经存在这个妖怪,取两者最大值,赋值给列表中真实名字的那个记录
                if (killInfo.getKillBogey().containsKey(realName)) {
                    killInfo.getKillBogey().put(realName, Math.max(killInfo.getKillBogey().get(realName), killInfo.getKillBogey().get(name)));
                } else {
                    //如果不存在,记录
                    waitAdd.put(realName, killInfo.getKillBogey().get(name));
                }
                //最后记录这个要删除的别名
                waitRemove.add(name);
            }
        }

        //删除别名
        waitRemove.forEach(killInfo.getKillBogey()::remove);
        //添加别名转换后的妖怪
        killInfo.getKillBogey().putAll(waitAdd);

        Map<Place, Integer> killSumMap = new HashMap<>();

        //System.out.println(bogeys);

        for (String name : killInfo.getKillBogey().keySet()) {

            List<Place> places = data.getBogeyPlace().get(name);

            //过滤秘闻御魂限制
            places = mwyhFilter(killInfo.getMwyhLimit(), places);
            //过滤关卡限制
            places = levelFilter(killInfo.getLevelLimit(), places);

            for (Place place : places) {
                //只累计需要的数量,比如虽然副本可以打3个,但你此时只需要1个,那么这个数量优先的策略中,只能记+1
                Integer needNum = Math.min(place.getBogeyNum(), killInfo.getKillBogey().get(name));
                killSumMap.merge(place, needNum, Integer::sum);
            }
        }

        //System.out.println(killSumMap);

        //找到目前可以消除最多妖怪的副本
        Place maxPlace = null;
        int maxNum = 0;
        for (Place place : killSumMap.keySet()) {

            boolean replace = false;

            if (killSumMap.get(place) > maxNum) {
                replace = true;
            } else {
                if (killSumMap.get(place) == maxNum) {
                    //如果数量相等,按以下优先级规则判定
                    //1.同类别副本,等级低者优先
                    //2.不同类别副本,故事>御魂>秘闻
                    if (isSameType(place, maxPlace)) {
                        if (getLevel(place) < getLevel(maxPlace)) {
                            replace = true;
                        }
                    } else {
                        if (placeToPriority(place) > placeToPriority(maxPlace)) {
                            replace = true;
                        }
                    }
                }
            }

            if (replace) {
                maxNum = killSumMap.get(place);
                maxPlace = place;
            }
        }

        //System.out.println(maxPlace);

        //记录方案
        if (maxPlace != null) {
            res.merge(maxPlace.getName() + maxPlace.getKill(), 1, Integer::sum);
            //根据方案,扣减妖怪数量
            for (Iterator<Map.Entry<String, Integer>> it = killInfo.getKillBogey().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> bogeyKill = it.next();
                //这里和之前计算最小次数一样,需要获得这个妖怪的副本信息
                Optional<Place> place = getBogeyPlace(bogeyKill.getKey(), maxPlace);
                if (place.isPresent()) {
                    int remain = Math.max(0, bogeyKill.getValue() - place.get().getBogeyNum());
                    if (remain == 0) {
                        it.remove();
                    } else {
                        bogeyKill.setValue(remain);
                    }
                }
            }
        }

        //System.out.println(bogeys);

        if (killInfo.getKillBogey().size() > 0) {
            doCal(res, killInfo);
        }
    }

    private Optional<Place> getBogeyPlace(String bogeyName, Place commonPlace) {
        return data.getBogeyPlace().get(bogeyName).stream().filter(
                place -> place.getName().equals(commonPlace.getName()) &&
                        place.getKill().equals(commonPlace.getKill())).findFirst();
    }

    //过滤秘闻或者御魂限制条件
    private List<Place> mwyhFilter(Map<String, Integer> mwyhLimit, List<Place> places) {
        if (!mwyhLimit.isEmpty()) {
            return places.stream()
                    .filter(p -> !mwyhLimit.containsKey(p.getName()) || Integer.parseInt(p.getKill().substring(0, p.getKill().length() - 1)) <= mwyhLimit.get(p.getName()))
                    .collect(Collectors.toList());
        }
        return places;
    }

    //过滤如果存在关卡限制条件
    private List<Place> levelFilter(Integer levelLimit, List<Place> places) {
        return places.stream()
                .filter(p -> !isStory(p) || Integer.parseInt(p.getName().substring(0, p.getName().length() - 1)) <= levelLimit)
                .collect(Collectors.toList());
    }

    //副本名字转类型优先级
    private int placeToPriority(Place place) {
        String key = "御魂";
        if (isStory(place)) {
            key = "故事";
        } else if (!isYh(place)) {
            key = "秘闻";
        }
        return priority.get(key);
    }

    //获得层数
    private int getLevel(Place place) {
        if (isStory(place)) {
            return Integer.parseInt(place.getName().substring(0, place.getName().length() - 1));
        } else {
            return Integer.parseInt(place.getKill().substring(0, place.getKill().length() - 1));
        }
    }

    private boolean isStory(Place place) {
        return place.getName().contains("章");
    }

    private boolean isYh(Place place) {
        return place.getName().equals("御魂");
    }

    private boolean isMw(Place place) {
        return !isStory(place) && !isYh(place);
    }

    private boolean isSameType(Place one, Place two) {
        return (isStory(one) && isStory(two)) ||
                (isYh(one) && isYh(two)) ||
                (isMw(one) && isMw(two));
    }

    Data getData() {
        return data;
    }
}
