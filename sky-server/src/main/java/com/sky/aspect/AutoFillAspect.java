package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * 自定义切面，用于数据自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut(" execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void AutoFillpointcut() {}
    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("AutoFillpointcut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始公共字段填充");
        //方法签名对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();


        //获取实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {return;}


        Object entity = args[0];

        //准备新数据
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();


        if (operationType == OperationType.INSERT) {


            Method SET_CREATE_TIME = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method SET_CREATE_USER = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method SET_UPDATE_TIME = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method SET_UPDATE_USER = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            SET_CREATE_TIME.invoke(entity, now);
            SET_CREATE_USER.invoke(entity, id);
            SET_UPDATE_TIME.invoke(entity, now);
            SET_UPDATE_USER.invoke(entity, id);


        }
        else if(operationType == OperationType.UPDATE) {

            Method SET_UPDATE_TIME = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
            Method SET_UPDATE_USER = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

            SET_UPDATE_TIME.invoke(entity,now);
            SET_UPDATE_USER.invoke(entity,id);
        }

    }

}
