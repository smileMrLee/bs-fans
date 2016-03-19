package com.eden.fans.bs.web.aop;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by Administrator on 2016/3/19.
 */
@Component
@Aspect
public class ControllerAop {
    private static Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    private static Gson gson = new Gson();
    // 配置切入点
    @Pointcut(value = "execution(public java.lang.String com.eden.fans.bs.web.controller..*.*(..))")
    public void aspect() {
    }

    /**
     * controller处理：
     * 1.打印返回报文全量信息
     * 2.监控接口响应时间，讨论是否要做。
     * 3.其他。
     * */
    @Around("aspect()")
    public Object call(JoinPoint joinPoint) throws Throwable {
        ProceedingJoinPoint pjp = ((ProceedingJoinPoint) joinPoint);
        String executeMethod = pjp.getTarget().getClass().getPackage().getName() + "." + StringUtils.substringBefore(pjp.getSignature().toShortString(), "(");
        try {
            //To do 是否需要打印时间慢的接口。
            Object objectRtn = pjp.proceed();
            String responseJson = gson.toJson(objectRtn);
            logger.error("执行方法executeMethod：{},返回报文：{}",executeMethod,responseJson);
            return objectRtn;
        } catch (Exception e) {
            //已经是最后面向客户流程，必须处理
            throw new Exception(executeMethod,e);
        }catch(Throwable e){
            //已经是最后面向客户流程，必须处理
            throw new Exception(executeMethod,e);
        }finally {

        }
    }

}
