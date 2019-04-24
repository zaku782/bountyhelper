package com.zhstar.bountyhelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BountyhelperApplicationTests {

    @Autowired
    Calculate calculate;

    @Test
    public void contextLoads() {
        Map<String, Integer> bogeys = new HashMap<>();
        Map<String, Integer> mwyhLimit = new HashMap<>();
        bogeys.put("盗墓小鬼", 8);
        bogeys.put("丑时之女", 2);
        bogeys.put("大天狗", 3);
        bogeys.put("灯笼鬼", 11);
        Map<String, Integer> res = new HashMap<>();
        KillInfo killInfo = new KillInfo();
        killInfo.setKillBogey(bogeys);
        killInfo.setMwyhLimit(mwyhLimit);
        killInfo.setLevelLimit(28);
        calculate.doCal(res, killInfo);
        System.out.println(res);
    }

}
