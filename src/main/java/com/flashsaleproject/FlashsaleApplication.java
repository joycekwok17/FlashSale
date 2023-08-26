package com.flashsaleproject;

import com.flashsaleproject.dao.UserDOMapper;
import com.flashsaleproject.dataObject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xc
 */
@SpringBootApplication(scanBasePackages = {"com.flashsaleproject"})
//@Configuration
@RestController
@MapperScan("com.flashsaleproject.dao")
public class FlashsaleApplication {

    @Autowired
    private UserDOMapper userDOMapper;

    public static void main(String[] args) {
        System.out.println("FlashsaleApplication is running...");
        SpringApplication.run(FlashsaleApplication.class, args);
    }

    @RequestMapping("/")
    public String home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(2);
        if (userDO == null) {
            return "User not found!";
        } else {
            return userDO.getName();
        }
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
