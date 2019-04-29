package com.zxl.elasticjobspringboot.controller;

import com.zxl.elasticjobspringboot.config.ElasticJobConfig;
import com.zxl.elasticjobspringboot.quartz.MySimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe
 * @Author zxl
 * @Date 2019-04-29 11:05
 */
@RestController
public class TestController {

    @Autowired
    private ElasticJobConfig elasticJobConfig;

    @RequestMapping("/addJob")
    public void addJob() {
        int shardingTotalCount = 2;
        elasticJobConfig.addSimpleJobScheduler(new MySimpleJob().getClass(), "* * * * * ?", shardingTotalCount, "0=A,1=B");
    }
}
