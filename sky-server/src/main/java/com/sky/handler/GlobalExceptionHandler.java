package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration;

    public GlobalExceptionHandler(SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration) {
        this.sqlInitializationAutoConfiguration = sqlInitializationAutoConfiguration;
    }

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String msg = ex.getMessage();
        String sqlMas = "";
        if(msg.contains("Duplicate entry")){
            String[] split = msg.split(" ");

            sqlMas = split[2] + MessageConstant.ALREADY_EXISTS;

        }else{
            sqlMas = MessageConstant.UNKNOWN_ERROR;
        }

        return Result.error(sqlMas);
    }

}
