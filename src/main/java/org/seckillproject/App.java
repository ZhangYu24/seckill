package org.seckillproject;

import org.mybatis.spring.annotation.MapperScan;
import org.seckillproject.dao.UserDOMapper;
import org.seckillproject.dataobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"org.seckillproject"})
@RestController
@MapperScan("org.seckillproject.dao")
public class App {
    
    @Autowired
    private UserDOMapper userDOMapper;
    
    @RequestMapping("/")
    public String home(){

        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null){
            return "用户不存在";
        }else {
            return userDO.getName();
        }
        
        
        
    }
    public static void main( String[] args ) {
        SpringApplication.run(App.class);
    }
}
