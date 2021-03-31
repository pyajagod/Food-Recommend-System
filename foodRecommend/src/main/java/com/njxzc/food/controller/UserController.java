package com.njxzc.food.controller;

import com.alibaba.fastjson.JSONObject;
import com.njxzc.food.dao.UserDao;
import com.njxzc.food.model.User;
import com.njxzc.food.util.ImgConfig;
import com.njxzc.food.util.Layui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;

//    @Value("${food.capture.image.path}")
//    private String captureImagePath;
//
//    @Value("${file.uploadFolder}")
//    private String uploadFolder;

    @PostMapping("/save")
    public void save(User user, HttpServletResponse response) throws IOException {
//        System.out.println(user);
        userDao.save(user);
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
    @GetMapping("/reg")
    public void reg(User user, HttpServletResponse response) throws IOException {
//        System.out.println(user);
        user.setAvatar("/upload/erha.png");
        userDao.save(user);
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }

    @GetMapping("/login")
    public void Login(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> by = userDao.findBy(user);

        if (by.size() == 1){
            User us = by.get(0);
            Cookie cookie1 = new Cookie("fuid", us.getUid()+"");
            Cookie cookie2 = new Cookie("fusername", us.getUsername());
            Cookie cookie3 = new Cookie("favatar", us.getAvatar());
            cookie1.setMaxAge(60*60*3);
            cookie2.setMaxAge(60*60*3);
            cookie3.setMaxAge(60*60*3);
            cookie1.setDomain("html");
            cookie2.setDomain("html");
            cookie3.setDomain("html");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
//            int length = request.getCookies().length;
//            System.out.println(length);
            Layui data = Layui.data3(us);
            Layui.PrintClose(data, response);
//            System.out.println(",.................");
//            request.getRequestDispatcher("index.html").forward(request, response);
        }else {
//            Layui data = Layui.data2("no");
//            Layui.PrintClose(data, response);
//            request.getRequestDispatcher("user/login.html").forward(request, response);
            response.sendRedirect("/user/login.html");
        }
    }

    @ResponseBody
    @GetMapping("/getUser")
    public String user(String uid){
        System.out.println(uid);
        User one = userDao.findOne(Integer.valueOf(uid));
        System.out.println(one);
        return JSONObject.toJSONString(one);
    }

    @RequestMapping("/show")
    public void showUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        List<User> users = userDao.findAll();
        Layui data = Layui.data(users.size(), users);
        Layui.PrintClose(data, response);
    }

    @RequestMapping("/showBy")
    public void showBy(String id, User user, HttpServletResponse response, HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if (!"".equals(id)) {
            user.setUid(Long.parseLong(id));
        }
        List<User> users = userDao.findBy(user);
        Layui data = Layui.data(users.size(), users);
        Layui.PrintClose(data, response);
    }

    @RequestMapping("/upload")
    public void uploadHeadPortrait(MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        if (file == null){
            return;
        }
        //保存图片
        String fileName = file.getOriginalFilename();
        //文件后缀名
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        String destFileName = UUID.randomUUID().toString().replace("-", "")+prefix;

        File fileUrl = new File(ImgConfig.uploadFolder + ImgConfig.captureImagePath);
        if(!fileUrl.exists()){
            fileUrl.mkdirs();
        }

        File destImage = new File(fileUrl, destFileName);
        try {
            file.transferTo(destImage);
        }catch (IOException e){
            System.out.println("图片保存失败");
        }
        String src = "/upload/" + destFileName;
//        System.out.println(src);
        Layui.PrintClose(Layui.data2(src), response);
    }

    @PostMapping("/update")
    public void update(User user, HttpServletResponse response) throws IOException {
        System.out.println(user);
        userDao.update(user);
        Layui data = Layui.data2("okk");
//        response.sendRedirect("/admin/main.html");
        Layui.PrintClose(data, response);
    }

    @PostMapping("/delete")
    public void delete(String uid, HttpServletResponse response) throws IOException {
        userDao.delete(Integer.parseInt(uid));
        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }

    @PostMapping("/deleteMore")
    public void deleteMore(@RequestParam(value = "ids", required = true) String ids, HttpServletResponse response) throws IOException {

        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            int Id = Integer.parseInt(id[i]);
            userDao.delete(Id);
//            System.out.println(Id);
        }

        Layui data = Layui.data2("yes");
        Layui.PrintClose(data, response);
    }
}
