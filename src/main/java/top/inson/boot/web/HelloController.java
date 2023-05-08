package top.inson.boot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jingjitree
 * @description
 * @date 2023/4/20 9:52
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @GetMapping("/say")
    public String say(){

        return "你好，世界";
    }

}
