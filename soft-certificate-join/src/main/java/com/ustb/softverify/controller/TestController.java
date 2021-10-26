package com.ustb.softverify.controller;

import com.ustb.softverify.entity.vo.UserVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/visit")
    public void visit(@RequestParam("username") String username, HttpServletResponse resp) throws IOException {
        System.out.println(username);
        resp.sendRedirect("redirect:http://localhost:9527/#/soft/info?username="+username);
    }

    @GetMapping("/alipayforward")
    public ModelAndView alipayforward(@RequestParam("username") String username,
                                      HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println(username);
        String url = "redirect:http://localhost:9527/#/soft/info?username="+username;
        return new ModelAndView(url);
    }
}
