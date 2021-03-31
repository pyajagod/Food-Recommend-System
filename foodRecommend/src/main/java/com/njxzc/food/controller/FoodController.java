package com.njxzc.food.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njxzc.food.dao.FoodDao;
import com.njxzc.food.dao.RateDao;
import com.njxzc.food.model.Food;
import com.njxzc.food.model.Rates;
import com.njxzc.food.util.Layui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodDao foodDao;

    @Autowired
    private RateDao rateDao;


    @RequestMapping("/save")
    public void save(Food food, HttpServletResponse response) throws IOException {
//        System.out.println(food);
        foodDao.save(food);
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
    @RequestMapping("/show")
    public void Show(HttpServletResponse response) throws IOException {
        List<Food> all = foodDao.getAll();
//        System.out.println(all.get(0));
        response.setContentType("text/html;charset=utf-8");
        Layui data = Layui.data(1, all);
        Layui.PrintClose(data, response);
    }

    @ResponseBody
    @GetMapping("/showOne")
    public String showOne(String fid, String uid, HttpServletResponse response){
        Food food = foodDao.findOne(Integer.valueOf(fid));
        Rates rates = rateDao.findRates(Integer.parseInt(fid), Integer.parseInt(uid));
//        System.out.println(rates);
        rateDao.updateRedis(rates);
        String data = "uid:"+rates.getUid() + " fid:" + rates.getFid() + " score:" + rates.getScore() + " shijian:" + rates.getTimestamp();
//        System.out.println(data);
        if (food.getChecked() == null){
            food.setChecked(Long.valueOf(1));
        }else {
            food.setChecked(food.getChecked() + 1);
        }
        foodDao.update(food);
        response.setContentType("text/html;charset=utf-8");
        return JSONObject.toJSONString(food);
    }

    @PostMapping("/showByc")
    @ResponseBody
    public String ShowByC(String canteen, HttpServletResponse response) throws IOException {
        Food food = new Food();
        switch (canteen){
            case "n1":
                food.setCanteen("南食堂一楼");
                break;
            case "n2":
                food.setCanteen("南食堂二楼");
                break;
            case "n3":
                food.setCanteen("南食堂三楼");
                break;
            case "b1":
                food.setCanteen("北食堂一楼");
                break;
            case "b2":
                food.setCanteen("北食堂二楼");
                break;
            case "b3":
                food.setCanteen("北食堂三楼");
                break;
            default:
                break;
        }
        response.setContentType("text/html;charset=utf-8");
        List<Food> foods = foodDao.findBy(food);
        return JSONArray.toJSONString(foods);
//        Layui data = Layui.data(1, foods);
//        Layui.PrintClose(data, response);
    }

    @GetMapping("/showByN")
    @ResponseBody
    public String ShowByN(String fname, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
//        System.out.println(fname);
        Food food = new Food();
        food.setName(fname);

        List<Food> foods = foodDao.findBy(food);
        return JSONArray.toJSONString(foods);
//        Layui data = Layui.data(1, foods);
//        Layui.PrintClose(data, response);
    }

    @PostMapping("/showBy")
    public void ShowBy(Food food, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
//        System.out.println(food);
        List<Food> foods = foodDao.findBy(food);
        Layui data = Layui.data(1, foods);
        Layui.PrintClose(data, response);
    }

    @PostMapping("/update")
    public void Update(Food food, HttpServletResponse response) throws IOException {
        foodDao.update(food);
        Layui data = Layui.data2("okk");
//        response.sendRedirect("/admin/main.html");
        Layui.PrintClose(data, response);
    }

    @PostMapping("/delete")
    public void Delete(String fid, HttpServletResponse response) throws IOException {
//        foodDao.deleteOne(food.getFid());
        foodDao.deleteOne(Long.valueOf(fid));
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
    @PostMapping("deleteMore")
    public void deleteMore(@RequestParam(value = "ids", required = true) String ids, HttpServletResponse response) throws IOException {

        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            int Id = Integer.parseInt(id[i]);
            foodDao.deleteOne(Long.valueOf(Id));
//            System.out.println(Id);
        }

        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
}
