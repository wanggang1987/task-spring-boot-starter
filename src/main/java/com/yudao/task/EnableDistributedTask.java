package com.yudao.task;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DistributedTaskConfig.class})
public @interface EnableDistributedTask {
}
