package com.njxzc.food.controller;

import com.njxzc.food.dao.AdminDao;
import com.njxzc.food.model.Admin;
import com.njxzc.food.util.Layui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminDao adminDao;

    @RequestMapping("/save")
    public void Save(Admin admin, HttpServletResponse response) throws IOException {
//        System.out.println(admin);
        adminDao.save(admin);
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }

    @PostMapping("/update")
    public void Update(Admin admin, HttpServletResponse response) throws IOException {
//        System.out.println(admin);
        adminDao.update(admin);
        Layui data = Layui.data2("okk");
//        response.sendRedirect("/admin/main.html");
        Layui.PrintClose(data, response);
    }

    @RequestMapping("/show")
    public void Show(HttpServletResponse response) throws IOException {
        List<Admin> admins = adminDao.showAll();
        Layui data = Layui.data(1, admins);
        Layui.PrintClose(data, response);
    }
    @RequestMapping("/showBy")
    public void ShowBy(Admin admin, HttpServletResponse response) throws IOException {
        if (adminDao.showBy(admin).size() == 1){
            Layui data = Layui.data(1, adminDao.showBy(admin));
            Layui.PrintClose(data, response);
        }
    }
    @PostMapping("/showBys")
    public void ShowBys(Admin admin, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        System.out.println(admin);
        Layui data = Layui.data(1, adminDao.showBy(admin));
//        System.out.println(data);
        Layui.PrintClose(data, response);
//        }
    }

    @PostMapping("/delete")
    public void Delete(String id, HttpServletResponse response) throws IOException {
        adminDao.delete(Integer.parseInt(id));
//        System.out.println(id);
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
}
