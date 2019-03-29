package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.web.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    @PostMapping("accredit")
    public ResponseEntity<Void> authentication( String username, String password){


        String token=authService.authentication(username,password);

        if (StringUtils.isBlank(token)) {



            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //token不为空,将token回写值cookie
        return ResponseEntity.ok().build();
    }
}
