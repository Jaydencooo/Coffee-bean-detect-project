package com.coffee.project.service.impl;

import com.coffee.project.domain.CoffeeBeanGradeInfo;
import com.coffee.project.mapper.CoffeeBeanGradeInfoMapper;
import com.coffee.project.service.CoffeeBeanGradeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CoffeeBeanGradeInfoServiceImpl implements CoffeeBeanGradeInfoService {


    @Autowired
    private CoffeeBeanGradeInfoMapper coffeeBeanGradeInfoMapper;

    @Override
    public void UploadSampleImage(Long beanId,String sampleImageUrl) {

        CoffeeBeanGradeInfo coffeeBeanGradeInfo = coffeeBeanGradeInfoMapper.selectById(beanId);
        coffeeBeanGradeInfo.setSampleImageUrl(sampleImageUrl);
        coffeeBeanGradeInfoMapper.updateById(coffeeBeanGradeInfo);

    }
}
