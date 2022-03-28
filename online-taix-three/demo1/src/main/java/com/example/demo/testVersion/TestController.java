package com.example.demo.testVersion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ApiVersion
@RestController
@RequestMapping("api/{version}/")
//编写接口，标记上相应的 @ApiVersion

public class TestController {

    @GetMapping("/test")
    public String test01(@PathVariable String version) {
        return "test01 : " + version;
    }

    @GetMapping("/test")
    @ApiVersion(2)
    public String test02(@PathVariable String version) {
        return "test02 : " + version;
    }


    @GetMapping("/test03")
    public String test03() {
        return "test03: ";
    }

}
