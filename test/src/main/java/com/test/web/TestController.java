package com.test.web;

import com.test.entity.A;
import com.test.entity.B;
import com.test.service.AService;
import com.test.service.BService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author ALazyDogXD
 * @date 2022/4/17 17:04
 * @description test
 */

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private List<AService> aService;

    @Resource
    private BService bService;

    @GetMapping
    public void test() {
        aService.get(0).save(new A() {{
            setId(3);
            setTest("1q2dc1");
        }});
    }

    @GetMapping("test")
    public void testTest() {

    }

}
