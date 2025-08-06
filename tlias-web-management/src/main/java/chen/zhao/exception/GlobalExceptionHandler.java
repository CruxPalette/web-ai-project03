package chen.zhao.exception;

import chen.zhao.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序出错啦～", e);
        return Result.error("对不起,操作失败,请联系管理员~~~");
    }
    //处理异常


//    按照继承关系从下往上匹配
    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("程序出错啦～", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errMsg = message.substring(i);
        String[] arr = errMsg.split(" ");
        return Result.error(arr[2] + "已存在");
    }

    /**
     * 声明异常处理的方法 - BusinessException
     */
    @ExceptionHandler
    public Result handleBuinessException(BusinessException businessException) {
        log.error("服务器异常", businessException);
        return Result.error(businessException.getMessage());
    }



}
