package com.example.library;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Chú ý thay @RestController thành @Controller
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "index";  // Tên file index.html trong thư mục templates
    }
}
