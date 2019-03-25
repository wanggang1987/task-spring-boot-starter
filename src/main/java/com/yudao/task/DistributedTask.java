package com.yudao.task;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.yudao.util.HostUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.List;

public class DistributedTask {

    private String serviceName;
    private int servicePort;
    private String dockerIp;
    private DiscoveryClient discoveryClient;

    public DistributedTask(String serviceName, int servicePort, String dockerIp, DiscoveryClient discoveryClient) {
        this.serviceName = serviceName;
        this.servicePort = servicePort;
        this.dockerIp = dockerIp;
        this.discoveryClient = discoveryClient;
    }

    public boolean isMaster() {
        try {
            if (discoveryClient.getInstances(serviceName).isEmpty()) {
                // the first register
                return true;
            } else {
                return isSelfEarliestRegistered(discoveryClient.getInstances(serviceName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Long getRegistrationTime(EurekaDiscoveryClient.EurekaServiceInstance instance) {
        InstanceInfo o = instance.getInstanceInfo();
        LeaseInfo info = o.getLeaseInfo();
        return info.getRegistrationTimestamp();
    }

    private boolean isSelfEarliestRegistered(List<ServiceInstance> instances) throws UnknownHostException {
        EurekaDiscoveryClient.EurekaServiceInstance earliestInstance =
                (EurekaDiscoveryClient.EurekaServiceInstance) instances.get(0);
        Long earliestTime = System.currentTimeMillis();
        for (ServiceInstance instance : instances) {
            EurekaDiscoveryClient.EurekaServiceInstance tmp = (EurekaDiscoveryClient.EurekaServiceInstance) instance;
            long tmpTime = getRegistrationTime(tmp);
            if (tmpTime < earliestTime) {
                earliestTime = tmpTime;
                earliestInstance = tmp;
            }
        }

        String localIp = HostUtil.getLocalIp();
        if (!StringUtils.isEmpty(dockerIp)) {
            localIp = dockerIp;
        }

        if (earliestInstance.getPort() == servicePort
                && earliestInstance.getInstanceInfo().getIPAddr().equals(localIp)) {
            return true;
        }
        return false;
    }
}
