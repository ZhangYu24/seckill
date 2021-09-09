package org.seckillproject.service;


import org.seckillproject.error.BusinessException;
import org.seckillproject.service.model.UserModel;

/**
 * @author:ZhangYu
 * @date:2021/09/05 22:12
 */
public interface UserService {
    
    //通过用户id获取用户对象
    UserModel getUserById(Integer id);
    
    //注册用户
    void register(UserModel userModel) throws BusinessException;

}
