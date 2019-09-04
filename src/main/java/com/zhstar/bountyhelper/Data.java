package com.zhstar.bountyhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
class Data {

    Logger logger = LoggerFactory.getLogger(Data.class);

    //妖怪副本分布
    private Map<String, List<Place>> bogeyPlace = new HashMap<>();
    //妖怪列表
    private Map<String, List<String>> bogeys = new HashMap<>();
    //妖怪谜语对应
    private Map<String, String> puzzleMap = new HashMap<>();

    public Data(@Value("${bogey.place.file}") String bogeyPlaceFile) throws IOException {

        logger.info("==================加载数据========================");

        //加载副本数据
        BufferedReader br = new BufferedReader(new FileReader(bogeyPlaceFile));
        String one = null;
        while ((one = br.readLine()) != null) {
            String[] info = one.split(";");

            //加载妖怪分布副本数据
            List<Place> places = bogeyPlace.get(info[0]);

            Place place = new Place(info[1], info[2], Integer.parseInt(info[3]));
            if (places == null) {
                places = new ArrayList<>();
                places.add(place);
                bogeyPlace.put(info[0], places);
            } else {
                places.add(place);
            }

            //加载妖怪列表
            if (info.length >= 5) {
                addBogey(info[4], info[0]);
                if (info.length == 6) {
                    String puzzle = info[5];
                    List<String> puzzles = new ArrayList<>();
                    if (puzzle.contains("@")) {
                        puzzles.addAll(Arrays.asList(puzzle.split("@")));
                    } else {
                        puzzles.add(puzzle);
                    }
                    puzzles.forEach(p -> {
                        addBogey(p.split(":")[1], p.split(":")[0]);
                        puzzleMap.put(p.split(":")[0], info[0]);
                    });
                }
            }
            //妖怪谜语列表
        }
        br.close();
    }

    //根据首字母,放入妖怪列表
    private void addBogey(String alpha, String name) {
        List<String> aBogeys = bogeys.get(alpha);
        if (aBogeys == null) {
            aBogeys = new ArrayList<>();
            aBogeys.add(name);
            bogeys.put(alpha, aBogeys);
        } else {
            aBogeys.add(name);
        }
    }

    Map<String, List<Place>> getBogeyPlace() {
        return bogeyPlace;
    }

    Map<String, List<String>> getBogeys() {
        return bogeys;
    }

    Map<String, String> getPuzzleMap() {
        return puzzleMap;
    }
}



