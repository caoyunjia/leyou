package com.leyou.gateway.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
@Slf4j
@Data
public class JwtProperties {
    private String pubKeyPath;
    private PublicKey publicKey;  //公钥
    private String cookieName;

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }

}
