package com.example.demo.testVersion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

@Slf4j
@Configuration
//配置注册自定义的 CustomRequestMappingHandlerMapping
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //@Override 不对
 /*   public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        System.out.println("bbbbb");
        return new CustomRequestMappingHandlerMapping();
    }*/
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping(
            @Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
            @Qualifier("mvcConversionService") FormattingConversionService conversionService,
            @Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
        System.out.println("aaaaaa");
        return new CustomRequestMappingHandlerMapping();

    }




    }
