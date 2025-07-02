package com.sparrowzoo.coder.adapter.controller;

import com.sparrow.utility.EnumUtility;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("coder")
@Api(value = "ProjectConfig", tags = "ProjectConfig")
public class CoderController {
    @RequestMapping("get-names-by-enum")
    public List<String> getNames(@RequestBody String enumFullName) throws ClassNotFoundException {
        return EnumUtility.getNames(enumFullName);
    }
}
