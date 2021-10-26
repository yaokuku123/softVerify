package com.ustb.softverify.controller;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.vo.ProjectVo;
import com.ustb.softverify.entity.vo.UserVo;
import com.ustb.softverify.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UploadService uploadService;

    @GetMapping("/visit")
    public void visit(@RequestParam("username") String username, HttpServletResponse resp) throws IOException {
        System.out.println(username);
        resp.sendRedirect("redirect:http://110.43.204.211/#/soft/info");
    }

    @GetMapping("/alipayforward")
    public ModelAndView alipayforward(@RequestParam("username") String username,
                                      HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println(username);
        String url = "redirect:http://110.43.204.211/#/soft/info?username="+username;
        return new ModelAndView(url);
    }

        /**
         * 接收全流程系统发送的数据
         * @param projectVo
         * @return
         */
//    @PostMapping("/softwareaudit")
//    public ModelAndView softwareaudit2(@RequestBody ProjectVo projectVo){
//        ProjectVo project = uploadService.getResponseInfo(projectVo);
//        String appliedinst = project.getAppliedinst();
//        String url = "redirect:http://110.43.204.211/#/soft/info?appliedinst="+appliedinst;
//        return new ModelAndView(url);
//    }
}
