package com.zy.shop.common.aspect;

import com.zy.shop.common.dto.response.BaseShopResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:56
 */

@Slf4j
@Component
@Aspect
public class RequestLoggerAspect{

    @Pointcut("@annotation(com.zy.shop.common.aspect.RequestLogger)")
    public void pointCut() {
    }

    @Around("pointCut()")
    @SuppressWarnings("unchecked")
    public BaseShopResponse<Object> doAround(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            RequestLogger requestLogger = signature.getMethod().getAnnotation(RequestLogger.class);
            String description = requestLogger.description();
            log.info("请求 " + description + " 入参：{}", joinPoint.getArgs());
            Object result = joinPoint.proceed();
            log.info("请求 " + description + " 出参：{}", result);
            return (BaseShopResponse<Object>) result;
        } catch (Throwable e) {
            log.info("执行拦截请求入参，出参报错：{}", e.getMessage(), e);
            return BaseShopResponse.fail("执行拦截请求入参，出参报错: " + e.getMessage());
        }
    }
}
