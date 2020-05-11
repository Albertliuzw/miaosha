package com.albert.miaosha.service;

import com.albert.miaosha.dao.MiaoshaUserDao;
import com.albert.miaosha.domain.MiaoshaUser;
import com.albert.miaosha.exception.GlobalException;
import com.albert.miaosha.redis.MiaoshaUserKey;
import com.albert.miaosha.redis.RedisService;
import com.albert.miaosha.result.CodeMsg;
import com.albert.miaosha.util.MD5Util;
import com.albert.miaosha.util.UUIDUtil;
import com.albert.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //System.out.println("UserService "+loginVo);
        //判断手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));

        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        String dbPass = miaoshaUser.getPassword();
        String dbSalt = miaoshaUser.getSalt();
        dbPass = MD5Util.inputPassToDBPass(dbPass, dbSalt);
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);

        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
