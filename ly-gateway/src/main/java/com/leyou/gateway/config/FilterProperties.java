package com.leyou.gateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "ly.filter")
@Slf4j
@Data
public class FilterProperties {

    private List<String> allowPaths;

}
