package com.albert.miaosha.controller;


import com.albert.miaosha.redis.RedisService;
import com.albert.miaosha.result.Result;
import com.albert.miaosha.service.MiaoshaUserService;
import com.albert.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    //sl4j
    private Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
       // System.out.println(loginVo.toString());
        //登录
        miaoshaUserService.login(response, loginVo);
        return Result.success(true);
    }
}
