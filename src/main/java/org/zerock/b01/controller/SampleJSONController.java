package org.zerock.b01.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleJSONController {

    @GetMapping("/helloArr")
    public String[] helloArr() {
        log.info("helloArr..........");
        return new String[]{"AAA", "BBB", "CCC"};
    }
}
