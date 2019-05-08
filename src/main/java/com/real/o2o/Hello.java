package com.real.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: mabin
 * @create: 2019/5/4 20:31
 */
@RestController
public class Hello {

    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public String hello(){
        return "Hello,Spring boot!";
    }
}
