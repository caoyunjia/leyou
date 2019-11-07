package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    /**
     * 登录授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    @ApiOperation(value = "登录授权，登录成功后并回写Cookie")
    public ResponseEntity<Void> authentication(@RequestParam(value = "username") String username,
                                               @RequestParam(value = "password") String password,
                                               HttpServletRequest request,
                                               HttpServletResponse response){


        String token=authService.authentication(username,password);

        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CookieUtils.setCookie(request,response,properties.getCookieName(),token);
        //token不为空,将token回写值cookie
        return ResponseEntity.noContent().build();
    }

    /**
     * 验证用户并返回用户信息,并刷新Cookie的值
     * @param token
     * @return
     */
    @GetMapping("verify")
    @ApiOperation(value = "验证Cookie里的信息，正确的话刷新Cookie")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token, HttpServletRequest request,
                                           HttpServletResponse response){
        if(StringUtils.isBlank(token)){
            throw new LyException(ExceptionEnums.UNAUTHORIZED);
        }
        PublicKey publicKey = properties.getPublicKey();
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);
            //刷新Cookie的有效时间
            String newToken = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
            CookieUtils.setCookie(request,response,properties.getCookieName(),newToken);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //token被篡改或者过期
            throw new LyException(ExceptionEnums.UNAUTHORIZED);
        }
    }
}
