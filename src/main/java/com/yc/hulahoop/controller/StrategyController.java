package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/strategy/")
public class StrategyController {

    @Autowired
    StrategyService strategyService;

    /**
     * 列出所有攻略
     *
     * @param pageNum  页码
     * @param cityId   城市编号(如果参数没有值就会默认赋值null,所以要用int的包装类)
     * @param duration 时长
     * @return 攻略集合
     */
    @RequestMapping(value = "list.action", method = RequestMethod.POST)
    @ResponseBody
    private ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "cityId", required = false) Integer cityId,
                                @RequestParam(value = "duration", required = false) String duration) {
        System.out.println("controller" + pageNum + "," + cityId + "," + duration);
        return strategyService.queryStrategyList(pageNum, cityId, duration);
    }

}
