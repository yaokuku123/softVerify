package com.ustb.softverify.handler;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.exception.ParamNullException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e){
        e.printStackTrace();
        return ResponseResult.error();
    }

    /**
     * 参数传递为空或Null错误异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ParamNullException.class)
    @ResponseBody
    public ResponseResult paramNullExceptionHandler(ParamNullException e) {
        e.printStackTrace();
        return ResponseResult.error().message("参数传递为空或者为Null异常");
    }

}
