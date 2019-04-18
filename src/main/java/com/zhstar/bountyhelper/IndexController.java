package com.zhstar.bountyhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    Data data;

    @RequestMapping("/bogeys")
    public Map<String, List<String>> index() {
        return data.getBogeys();
    }
}
