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

    /**
     * 编解码数据异常
     * @param e
     * @return
     */
    @ExceptionHandler(CodecException.class)
    public ResponseResult certificateUpChainExceptionHandler(CodecException e) {
        e.printStackTrace();
        return ResponseResult.error().message("编解码数据异常");
    }

    /**
     * 软件压缩包超过指定大小异常
     * @param e
     * @return
     */
    @ExceptionHandler(CompressSizeException.class)
    public ResponseResult certificateUpChainExceptionHandler(CompressSizeException e) {
        e.printStackTrace();
        return ResponseResult.error().message("软件压缩包超过指定大小异常");
    }

    /**
     * 软件压缩包数量不匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(CompressNumException.class)
    public ResponseResult certificateUpChainExceptionHandler(CompressNumException e) {
        e.printStackTrace();
        return ResponseResult.error().message("软件压缩包数量不匹配异常");
    }


    /**
     * 上传软件用户信息异常
     * @param e
     * @return
     */
    @ExceptionHandler(MisMatchContentException.class)
    public ResponseResult misMatchContentException(MisMatchContentException e) {
        e.printStackTrace();
        return ResponseResult.error().message("重要文档未匹配目录文件异常");
    }

    @ExceptionHandler(CoreFileMisException.class)
    public ResponseResult coreFileMisException(CoreFileMisException e) {
        e.printStackTrace();
        return ResponseResult.error().message("未上传目录文件异常");
    }

}
