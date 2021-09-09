package org.seckillproject.error;

import org.seckillproject.response.CommonReturnType;

/**
 * @author:ZhangYu
 * @date:2021/09/06 21:25
 */
public interface CommonError {
    
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
