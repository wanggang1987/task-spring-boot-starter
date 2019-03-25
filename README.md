# task-spring-boot-starter
a distributed task module

## version 0.1.0
Supporting distributed services, find the first service registered to Eureka as master, and then the master performs timing tasks.To support docker containers, you need to configure the environment variable "EUREKA_INSTANCE_IP_ADDRESS", which is the IP of the container service node.

use example as follows:

* step1: 
```
@SpringBootApplication
@EnableDiscoveryClient
@EnableDistributedTask
@EnableScheduling
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
* step2:
```
@Component
public class MySchedule {
    @OneTask
    @Scheduled(fixedRate = 5000)
    public void exec() {
        System.out.println("In distributed microservices, only one machine executes");
    }
}

```

