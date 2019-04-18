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
        bogeys.put("海坊主", 5);
        bogeys.put("汤碗/琴/牙牙", 10);
        bogeys.put("狸猫", 9);
        bogeys.put("天邪鬼赤", 12);
        mwyhLimit.put("小鹿男", 6);
        mwyhLimit.put("姑获鸟", 6);
        mwyhLimit.put("荒川", 6);
        mwyhLimit.put("御魂", 4);
        calculate.doCal(bogeys, mwyhLimit, 24);
        System.out.println(calculate.getRes());
    }

}
