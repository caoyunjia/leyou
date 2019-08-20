package com.leyou.user.web;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 注册用户
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam(value = "code") String code){

        //注册成功不进行报错
        userService.register(user,code);

        return ResponseEntity.ok().build();
    }


    /**
     * 发送验证码
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam(value = "phone") String phone){
        userService.sendCode(phone);
        return ResponseEntity.noContent().build();
    }

    /**
     * 校验用户名或者电话是否唯一
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data,@PathVariable(value = "type") Integer type){

        Boolean flag=userService.checkData(data,type);
        if (flag == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(flag);

    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(@RequestParam(value = "username") String username,
                                                               @RequestParam(value = "password") String password){
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username, password));

    }
}
