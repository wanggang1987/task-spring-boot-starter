package com.yudao.task;

import com.yudao.task.util.HostUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedTaskConfig {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int servicePort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public DistributedTask connectionFactory() {
        return new DistributedTask(serviceName, servicePort, HostUtil.getEnvIp(), discoveryClient);
    }

    @Bean
    public DistributedTaskAspect taskExecuteAspect(DistributedTask taskCountClient) {
        return new DistributedTaskAspect(taskCountClient);
    }
}
