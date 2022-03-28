package com.mashibing.cloudzuul.filter;

import com.mashibing.cloudzuul.service.CommGrayRuleService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.mashibing.cloudzuul.entity.CommGrayRule;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 49178
 * @create 2022/3/5
 *
 * 网关灰度
 */

@Component
public class GrayFilter extends ZuulFilter {

    @Autowired
    private CommGrayRuleService commGrayRuleService;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }  //路由过程中,pre也可以

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String requestURI = request.getRequestURI();
        System.out.println("requestURI:"+requestURI);
        String userId = request.getHeader("user_id");
        boolean flag =false;
        if (StringUtils.isNotBlank(userId)){
        List<CommGrayRule> rules = commGrayRuleService.getGrayRuleByUserId(Integer.parseInt(userId));
        System.out.println("rules:"+ToStringBuilder.reflectionToString(rules));

        if(!CollectionUtils.isEmpty(rules)){
            for (int i = 0; i < rules.size(); i++) {
                CommGrayRule rule =rules.get(i);
                if (requestURI.contains(rule.getServiceName())) {
                    System.out.println("从灰度规则表查询匹配上userId[" + userId + "]的serviceName:" + rule.getServiceName() + " 对应配置的version:" + rule.getVersion());
                    String version = rule.getVersion();
                    //主要是用来 进行zuul中转发的集群服务的筛选，
                    //RibbonFilterContextHolder.getCurrentContext().add("userId", "1") 则会转发跳往至设置为userId为1的那个集群。
                    //对于RibbonFilterContextHolder的使用是用来选集群中的某一个节点的，
                    RibbonFilterContextHolder.getCurrentContext().add("version", version);  //网关路由时，走metadata里设置的属性的对应服务 ribbon-discovery-filter-spring-cloud-starter
                    flag=true;
                    break;
                }
            }
        }
        }
        if (!flag){
            System.out.println("没有该用户的灰度发布配置配置。。。走默认版本");
            //没有配置灰度规则，走默认的
        }


        return null;
    }
}
