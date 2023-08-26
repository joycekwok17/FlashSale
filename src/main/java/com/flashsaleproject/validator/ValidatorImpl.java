//package com.flashsaleproject.validator;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import java.util.Set;
//
///**
// * @author Xuanchi Guo
// * @project flashsale
// * @created 8/26/23
// */
//@Component
//public class ValidatorImpl implements InitializingBean {
//    private Validator validator;
//
//    public ValidationResult validate(Object bean) {
//        ValidationResult result = new ValidationResult();
//        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
//
//        if (constraintViolationSet.size() > 0) {
//            //有错误
//            result.setHasErrors(true);
//            constraintViolationSet.forEach(constraintViolation -> {
//                String errMsg = constraintViolation.getMessage();
//                String propertyName = constraintViolation.getPropertyPath().toString();
//                result.getErrorMsgMap().put(propertyName, errMsg);
//            });
//        }
//        return result;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
//    }
//}
