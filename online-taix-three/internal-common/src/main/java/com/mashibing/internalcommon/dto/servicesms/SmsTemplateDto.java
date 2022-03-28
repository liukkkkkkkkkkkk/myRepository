package com.mashibing.internalcommon.dto.servicesms;

import lombok.Data;

import java.util.Map;

/**
 * @author 49178
 * @create 2022/2/18
 */
@Data
public class SmsTemplateDto {
    //模板id
    private String id;

    // 参数  占位符
    private Map<String, Object> templateMap;

    @Override
    public String toString() {
        return "SmsTemplateDto [id=" + id + ", templateMap=" + templateMap + "]";
    }
}
