package com.zxl.elasticjobspringboot.quartz;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;
import com.zxl.elasticjobspringboot.entiy.YfUserYfbaoAccountJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author zxl
 * @Date 2019-04-29 11:05
 * 这里分两片对业务进行操作(0=A,1=B)
 * 每天凌晨一点执行： 0 0 1 * * ?
 */
@ElasticSimpleJob(cron = "* * * * * ?", jobName = "test_zxl", shardingTotalCount = 2, jobParameter = "测试参数", shardingItemParameters = "0=A,1=B")
@Component
public class MySimpleJob implements SimpleJob {
    private static final Logger logger = LoggerFactory.getLogger("MySimpleJob");
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("------Thread ID: %s, 任务总片数: %s, " + "当前分片项: %s.当前参数: %s," + "当前任务名称: %s.当前任务参数: %s",
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()));
        // 分片数
        Integer shardingTotalCount = shardingContext.getShardingTotalCount();
        // 当前分片
        Integer shardingItem = shardingContext.getShardingItem();
        List<String> tmpList = new ArrayList<>();
        tmpList.add("1");
        tmpList.add("2");
        tmpList.add("3");
        tmpList.add("4");
        for (int i = 0; i < tmpList.size(); i++) {
            // 取模
            int j = i % shardingTotalCount;
            if (shardingItem == j) {
                //一批数据(即4条数)分别给两个分片来操作
                // 当前分片处理当期数据
                YfUserYfbaoAccountJob yfUserYfbaoAccountJob = new YfUserYfbaoAccountJob();
                yfUserYfbaoAccountJob.setName("zxl_"+i);
                yfUserYfbaoAccountJob.setId("123_"+i);
                yfUserYfbaoAccountJob.setSex("boy");
                String str = "当前数据ID：" + yfUserYfbaoAccountJob.getId() + "，当前分片为：" + shardingItem + " ,处理下标为：" + i + "数据";
                System.out.println(str);
                logger.info(str);
                doWork(yfUserYfbaoAccountJob);
            }
        }
    }

    public void doWork(YfUserYfbaoAccountJob yfUserYfbaoAccountJob){
        //做自己的插入的业务操作
    }

}
