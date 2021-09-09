package org.seckillproject.controller;

import com.alibaba.druid.util.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.seckillproject.controller.viewobject.UserVO;
import org.seckillproject.error.BusinessException;
import org.seckillproject.error.EmBusinessError;
import org.seckillproject.response.CommonReturnType;
import org.seckillproject.service.UserService;
import org.seckillproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author:ZhangYu
 * @date:2021/09/05 22:09
 */
@Controller("user") 
@RequestMapping("/user")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class UserController extends BaseController{
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HttpServletRequest httpServletRequest;
    
    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Byte gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, NoSuchAlgorithmException {
        //验证手机号和对应的otpcode相符合
        String inSessionOptCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        
        if (!StringUtils.equals(otpCode,inSessionOptCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));
        
        userService.register(userModel);
        
        return CommonReturnType.create(null);
        
    }
    
    public String EncodeByMD5(String str) throws NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        
        //加密字符串
        String newStr = base64en.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        
        return newStr;
    }
    
    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将otp验证码同对应用户关联,使用httpSession的方式绑定手机号与otpCode
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
        
        //将otp验证码通过短信通道发生给用户，省略
        System.out.println("telphone = " + telphone + "& otpCode = " + otpCode);
        return CommonReturnType.create(null);
        
    }
    
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        
        //若获取的对应用户信息不存在
        if (userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        
        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        
        return CommonReturnType.create(userVO);
         
    }
    
    private UserVO convertFromModel(UserModel userModel){
        
        if (userModel == null){
            return null;
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        
        return userVO;
    }
    
    
}
