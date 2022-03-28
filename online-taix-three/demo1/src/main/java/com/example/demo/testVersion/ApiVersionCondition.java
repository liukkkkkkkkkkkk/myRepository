package com.example.demo.testVersion;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
/**
 * 自定义url匹配逻辑
 *
 * 创建 ApiVersionCondition 类，并继承RequestCondition 接口，作用是：版本号筛选，将提取请求URL中版本号，与注解上定义的版本号进行比对，以此来判断某个请求应落在哪个controller上。*/
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * 接口路径中的版本号前缀，如: api/v[1-n]/test
     */
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+)/");

    private int apiVersion;

    ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 最近优先原则，方法定义的 @ApiVersion > 类定义的 @ApiVersion
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            // 获得符合匹配条件的ApiVersionCondition
            int version = Integer.valueOf(m.group(1));
            if (version >= getApiVersion()) {
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 当出现多个符合匹配条件的ApiVersionCondition，优先匹配版本号较大的
        return other.getApiVersion() - getApiVersion();
    }

}
