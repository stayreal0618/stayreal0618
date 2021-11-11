package com.bj58.pay.controller;

import com.bj58.pay.annotation.Autowire;
import com.bj58.pay.annotation.Controller;
import com.bj58.pay.service.UserService;

/**
 * @author yy
 * @version 1.0v
 * @description test
 * @date 2021/11/9 17:19
 */
@Controller
public class HelloController {

    @Autowire
    private UserService userService;


}
