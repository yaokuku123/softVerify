package com.ustb.softverify.handler;

import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
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
    public ResponseResult paramNullExceptionHandler(ParamNullException e) {
        e.printStackTrace();
        return ResponseResult.error().message("参数传递为空或者为Null异常");
    }

    /**
     * Json数据转换异常
     * @param e
     * @return
     */
    @ExceptionHandler(JsonTransferException.class)
    public ResponseResult jsonTransferExceptionHandler(JsonTransferException e) {
        e.printStackTrace();
        return ResponseResult.error().message("Json数据转换异常");
    }

    /**
     * 软件存储获取异常
     * @param e
     * @return
     */
    @ExceptionHandler(FileReadWriteException.class)
    public ResponseResult fileReadWriteExceptionHandler(FileReadWriteException e) {
        e.printStackTrace();
        return ResponseResult.error().message("软件存储获取异常");
    }

    /**
     * 文档路径信息不匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(DocPathMisMatchException.class)
    public ResponseResult docPathMisMatchExceptionHandler(DocPathMisMatchException e) {
        e.printStackTrace();
        return ResponseResult.error().message("文档路径信息不匹配异常");
    }

    /**
     * 压缩包解压失败异常
     * @param e
     * @return
     */
    @ExceptionHandler(DecompressFailException.class)
    public ResponseResult decompressFailExceptionHandler(DecompressFailException e) {
        e.printStackTrace();
        return ResponseResult.error().message("压缩包解压失败异常");
    }

    /**
     * 软件压缩包格式或大小错误异常
     * @param e
     * @return
     */
    @ExceptionHandler(CompressFormatException.class)
    public ResponseResult compressFormatExceptionHandler(CompressFormatException e) {
        e.printStackTrace();
        return ResponseResult.error().message("软件压缩包格式或大小错误异常");
    }

    /**
     * 证书上链存证异常
     * @param e
     * @return
     */
    @ExceptionHandler(CertificateUpChainException.class)
    public ResponseResult certificateUpChainExceptionHandler(CertificateUpChainException e) {
        e.printStackTrace();
        return ResponseResult.error().message("证书上链存证异常");
    }

}
