package com.coffee.project.controller;


import com.coffee.project.common.Result;
import com.coffee.project.service.CoffeeBeanGradeInfoService;
import com.coffee.project.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sample")
@Slf4j
public class UploadSampleImagesController {

    @Autowired
    private CoffeeBeanGradeInfoService coffeeBeanGradeInfoService;


    @PostMapping("upload")
    public Result<String> uploadSampleImage(@RequestParam("file") MultipartFile file,
                                            @RequestHeader("beanId") Long beanId) throws IOException {

        String sampleImageUrl = FileUploadUtils.upload(file);
        coffeeBeanGradeInfoService.UploadSampleImage(beanId,sampleImageUrl);

        return Result.success(sampleImageUrl);
    }

}
