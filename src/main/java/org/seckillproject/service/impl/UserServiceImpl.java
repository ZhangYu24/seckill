package org.seckillproject.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.seckillproject.dao.UserDOMapper;
import org.seckillproject.dao.UserPasswordDOMapper;
import org.seckillproject.dataobject.UserDO;
import org.seckillproject.dataobject.UserPasswordDO;
import org.seckillproject.error.BusinessException;
import org.seckillproject.error.EmBusinessError;
import org.seckillproject.service.UserService;
import org.seckillproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * @author:ZhangYu
 * @date:2021/09/05 22:12
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    
    @Override
    public UserModel getUserById(Integer id) {

        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        
        if (userDO == null){
            return null;
        }
        
        //通过拥护id获取对应的拥护加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        
        return convertFromDataObject(userDO,userPasswordDO);
    }
    
    //用户注册
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if(StringUtils.isEmpty(userModel.getName()) 
                || userModel.getAge() == null 
                || userModel.getGender() == null || StringUtils.isEmpty(userModel.getTelphone())){
            
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        
        //实现model -> dataobject
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }
       
        
        userModel.setId(userDO.getId());
        
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
        
        return;
        
    }
    
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        
        if (userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        
        return userPasswordDO;
    }    
    
    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        
        if (userDO == null){
            return null;
        }
        
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        
        if (userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        
        return userModel;
    }
}
