package com.pangpanglink.shortlink.admin.config;

import com.pangpanglink.shortlink.admin.biz.user.UserFlowRiskControlFilter;
import com.pangpanglink.shortlink.common.web.UserAuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用户配置自动装配
 */
@Configuration
public class UserConfiguration {

    /**
     * 用户鉴权过滤器
     */
    @Bean
    public FilterRegistrationBean<UserAuthFilter> globalUserAuthFilter(StringRedisTemplate stringRedisTemplate) {
        FilterRegistrationBean<UserAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserAuthFilter(stringRedisTemplate));
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    /**
     * 用户操作流量风控过滤器
     */
    @Bean
    @ConditionalOnProperty(name = "short-link.flow-limit.enable", havingValue = "true")
    public FilterRegistrationBean<UserFlowRiskControlFilter> globalUserFlowRiskControlFilter(
            StringRedisTemplate stringRedisTemplate,
            UserFlowRiskControlConfiguration userFlowRiskControlConfiguration) {
        FilterRegistrationBean<UserFlowRiskControlFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserFlowRiskControlFilter(stringRedisTemplate, userFlowRiskControlConfiguration));
        registration.addUrlPatterns("/api/short-link/admin/*");
        registration.setOrder(10);
        return registration;
    }
}
