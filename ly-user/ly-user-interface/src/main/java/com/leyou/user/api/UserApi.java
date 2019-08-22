package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @GetMapping("/query")
    User queryUserByUsernameAndPassword(@RequestParam(value = "username")String username,
                                                        @RequestParam(value = "password")String password);

}
