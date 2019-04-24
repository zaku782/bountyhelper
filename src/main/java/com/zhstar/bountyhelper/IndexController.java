package com.zhstar.bountyhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/server")
public class IndexController {

    @Autowired
    Calculate calculate;

    @RequestMapping("/bogeys")
    public Map<String, List<String>> index() {
        return calculate.getData().getBogeys();
    }

    @RequestMapping("/cal")
    public Map<String, Integer> cal(@RequestBody KillInfo killInfo) {
        Map<String, Integer> res = new TreeMap<>(
                Comparator.naturalOrder()
        );
        calculate.doCal(res, killInfo);
        return res;
    }
}
