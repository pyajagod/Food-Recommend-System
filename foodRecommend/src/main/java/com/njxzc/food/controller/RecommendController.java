package com.njxzc.food.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njxzc.food.dao.*;
import com.njxzc.food.model.*;
import com.njxzc.food.util.Layui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/recommend")
@Controller
public class RecommendController {

    @Autowired
    private HistoryFoodDao historyFoodDao;

    @Autowired
    private RecentlyFoodDao recentlyFoodDao;

    @Autowired
    private RateDao rateDao;

    @Autowired
    private UserRecsDao userRecsDao;

    @Autowired
    private FoodDao foodDao;


    @ResponseBody
    @PostMapping("/historyFood")
    public String HistoryRatingFoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        List<HistoryRateFoods> all = historyFoodDao.getAll();
        return JSONArray.toJSONString(all);
    }

    @ResponseBody
    @PostMapping("/recentlyFood")
    public String RecentlyRatingFoods(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        List<RecentlyRateFoods> all = recentlyFoodDao.getAll();
//        System.out.println(JSONArray.toJSONString(all));
        return JSONArray.toJSONString(all);
    }

    @PostMapping("/rate")
    public void RateFood(Rates rates, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        rateDao.save(rates);
        Layui yes = Layui.data2("yes");
        Layui.PrintClose(yes, response);
    }

    @ResponseBody
    @GetMapping("/favorite")
    public String FavoriteFood(String id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

//        System.out.println(id);
        List<Score> all = userRecsDao.all(Integer.parseInt(id));
        List<Food> foods = new ArrayList<>();
        for (Score score:all) {
            int fid = score.getFid();
            Food one = foodDao.findOne(fid);
            foods.add(one);
        }

        String s = JSONObject.toJSONString(foods);
        System.out.println(s);
        return s;
    }
}
