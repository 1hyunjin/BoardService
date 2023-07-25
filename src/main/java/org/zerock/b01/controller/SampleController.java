package org.zerock.b01.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello............");
        model.addAttribute("msg", "HELLO WORLD");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        List<String> list = Arrays.asList("AAA", "BBB", "CCC");

        model.addAttribute("list", list);
    }

//    @RequestMapping("/")
//    public String home() {
//        log.info("home controller");
//        return "home";
//    }
}
