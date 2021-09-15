package org.seckillproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author:ZhangYu
 * @date:2021/09/15 19:18
 */
@Component
public class ValidatorImpl implements InitializingBean {
    
    private Validator validator;
    
    //实现校验方法并返回校验结果
    public ValidationResult validator(Object bean){
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintValidationSet = validator.validate(bean);
        
        if (constraintValidationSet.size() > 0){
            result.setHasErrors(true);
            constraintValidationSet.forEach(constraintValidation -> {
                String errMsg = constraintValidation.getMessage();
                String propertyName = constraintValidation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        
        return result;

    }
        
    
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
