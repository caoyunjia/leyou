package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    private static final String KEY_PREFIX="user:verify:phone:";


    public Boolean checkData(String data, Integer type) {

        // 存储数据
        this.redisTemplate.opsForValue().set("key1", "value1");
        // 获取数据
        String val = this.redisTemplate.opsForValue().get("key1");
        System.out.println("val = " + val);

        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnums.INVALID_USER_DATA_TYPE);
        }
        return this.userMapper.selectCount(record) == 0;
    }

    public User queryUserByUsernameAndPassword(String username, String password) {
        // 查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            throw new LyException(ExceptionEnums.INVALID_USER_PASSWORD);
        }
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new LyException(ExceptionEnums.INVALID_USER_PASSWORD);
        }
        // 用户名密码都正确
        return user;
    }

    public void register(User user, String code) {
        //先去判断code与发送的短信是否一致
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!code.equals(cacheCode)){
            throw new LyException(ExceptionEnums.INVALID_VERIFY_CODE);
        }
        //校验
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    /**
     * 发送验证码
     * @param phone
     */
    public void sendCode(String phone) {
        String code = NumberUtils.generateCode(6);
        String key = KEY_PREFIX + phone;
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
        //保存验证码,用于注册的时候看
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
    }
}
