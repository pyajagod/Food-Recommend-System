package com.njxzc.food.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njxzc.food.model.Admin;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Layui extends HashMap<String, Object> {
    public static Layui data(Integer count, List<?> data){
        Layui r = new Layui();
        r.put("code", 0);
        r.put("msg", "");
        r.put("count", count);
        r.put("data", data);
        return r;
    }

    public static Layui data2(String data){
        HashMap<String, Object> src = new HashMap<>();
        src.put("src", data);
        Layui r = new Layui();
        r.put("code", 0);
        r.put("msg", "");
        r.put("data", src);
        return r;
    }
    public static Layui data3(Object object){
        HashMap<String, Object> src = new HashMap<>();
        src.put("src", object);
        Layui r = new Layui();
        r.put("code", 0);
        r.put("msg", "");
        r.put("data", src);
        return r;
    }


    public static Layui changeD(List<Admin> data){
        Layui r = new Layui();
        List<HashMap> ads = new ArrayList<>();
        for (int i=0; i<data.size(); i++) {
            HashMap<String, Object> ad = new HashMap<>();
            Admin a = data.get(i);
            ad.put("loginname", a.getAdminName());
            ad.put("telphone", a.getPhone());
            ad.put("email", a.getEmail());
            ad.put("jointime", a.getTime());
            ads.add(ad);
        }

        r.put("code", 0);
        r.put("msg", "");
        r.put("data", ads);
        return r;
    }

    public static void PrintClose(Layui data, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(data);
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
}
