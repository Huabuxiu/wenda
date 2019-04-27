package com.example.wenda.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.example.wenda.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        sb.append(" method "+joinPoint.getSignature()+": ");
        for (Object arg : joinPoint.getArgs()) {
            if(arg !=null){
                sb.append("arg:" + arg.toString() + "|");
            }
        }

        logger.info("before method: " + sb.toString().replace("com.example.wenda.controller.",""));
    }

    @After("execution(* com.example.wenda.controller.IndexController.*(..))")
    public void afterMethod() {
        logger.info("after method" + new Date());
    }
}
