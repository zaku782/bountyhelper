package com.zhstar.bountyhelper;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                request.getContextPath();
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Headers",
                        "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                //response.setHeader("Access-Control-Allow-Origin", "http://www.animalkindom.win");
                response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
                return true;
            }
        }).addPathPatterns("/**");
    }
}

