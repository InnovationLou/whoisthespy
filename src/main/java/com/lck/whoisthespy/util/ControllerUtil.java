package com.lck.whoisthespy.util;

import com.lck.whoisthespy.util.constant.RespCode;
import com.lck.whoisthespy.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 这个类封装着一些控制器中公共的代码块
 */
@Component
public class ControllerUtil {

    Logger logger = LoggerFactory.getLogger(ControllerUtil.class);


    public static ResponseVO getTrueOrFalseResult(boolean rs) {
        ResponseVO ResponseVO = new ResponseVO();
        if (rs)
            ResponseVO.success(null);
        else
            ResponseVO.error(RespCode.FAILURE_CODE, "方法执行时返回了false", "");
        return ResponseVO;
    }


    public static <T> ResponseVO<T> getDataResult(T data) {
        ResponseVO<T> ResponseVO = new ResponseVO<>();
        if (null != data)
            ResponseVO.success(data);
        else
            ResponseVO.error(RespCode.FAILURE_CODE, "ControllerUtil.getDataResult获得空的数据", null);
        return ResponseVO;
    }

    public static <T> ResponseVO<T> getSuccessResultBySelf(T data) {
        ResponseVO ResponseVO = new ResponseVO<>();
        ResponseVO.success(data);
        return ResponseVO;
    }

    public static <T> ResponseVO<T> getFalseResultMsgBySelf(String msg) {
        ResponseVO ResponseVO = new ResponseVO<>();
        ResponseVO.error(RespCode.FAILURE_CODE, msg, null);
        return ResponseVO;
    }

}
