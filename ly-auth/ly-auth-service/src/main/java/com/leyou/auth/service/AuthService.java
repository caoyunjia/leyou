package com.leyou.auth.service;


import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties props;

    public String authentication(String username, String password) {
        try {
            User user = userClient.queryUserByUsernameAndPassword(username, password);
            if(user==null){
                throw new LyException(ExceptionEnums.INVALID_USER_PASSWORD);
            }
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), props.getPrivateKey(), props.getExpire());
            return token;
        } catch (Exception e) {
            log.error("生成token失败,用户名称为[{}],错误信息[{}]",username,e);
            throw new LyException(ExceptionEnums.INVALID_USER_PASSWORD);
        }

    }
}
