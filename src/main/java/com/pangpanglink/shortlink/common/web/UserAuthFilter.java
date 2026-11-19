package com.pangpanglink.shortlink.common.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.pangpanglink.shortlink.common.biz.user.UserContext;
import com.pangpanglink.shortlink.common.biz.user.UserInfoDTO;
import com.pangpanglink.shortlink.common.constant.RedisCacheConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户鉴权过滤器
 */
public class UserAuthFilter extends OncePerRequestFilter {

    private final StringRedisTemplate stringRedisTemplate;

    public UserAuthFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isWhiteList(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        Object cached = null;
        if (StringUtils.hasText(username) && StringUtils.hasText(token)) {
            cached = stringRedisTemplate.opsForHash().get(RedisCacheConstant.USER_LOGIN_KEY + username, token);
        }
        if (cached == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(
                    AuthErrorResult.builder().status(401).message("Token validation error").build()));
            return;
        }
        JSONObject userJson = JSON.parseObject(cached.toString());
        UserContext.setUser(new UserInfoDTO(userJson.getString("id"), username, userJson.getString("realName")));
        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.removeUser();
        }
    }

    private boolean isWhiteList(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if ("POST".equals(method)
                && ("/api/short-link/admin/v1/user".equals(path)
                || "/api/short-link/admin/v1/user/login".equals(path))) {
            return true;
        }
        if ("GET".equals(method)
                && ("/api/short-link/admin/v1/user/has-username".equals(path)
                || "/page/notfound".equals(path))) {
            return true;
        }
        // 单段路径即短链跳转 /{short-uri}
        return "GET".equals(method) && path.matches("^/[^/]+$");
    }
}
