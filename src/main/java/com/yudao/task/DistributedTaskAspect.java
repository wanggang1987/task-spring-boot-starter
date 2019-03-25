package com.yudao.task;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * 任务执行决策切面
 */
@Order(1)
@Aspect
public class DistributedTaskAspect {
    static Logger logger = LoggerFactory.getLogger(DistributedTaskAspect.class);
    private DistributedTask distributedTask;

    public DistributedTaskAspect(DistributedTask taskCountClient) {
        this.distributedTask = taskCountClient;
    }

    @Around("@annotation(com.yudao.task.OneTask)")
    public void decisionTaskExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        if (distributedTask.isMaster()) {
            joinPoint.proceed();
        } else {
            logger.trace("joinPoint not execute {}", joinPoint.getSignature());
        }
    }
}
