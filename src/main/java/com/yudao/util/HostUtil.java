package com.yudao.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class HostUtil {

    private static InetAddress chooseAddress() throws UnknownHostException {
        Set<InetAddress> addresses = getAddresses();
        if (addresses.contains(InetAddress.getLocalHost())) {
            return InetAddress.getLocalHost();
        } else {
            return addresses != null && !addresses.isEmpty() ? ((InetAddress[]) addresses.toArray(new InetAddress[addresses.size()]))[0] : InetAddress.getLocalHost();
        }
    }

    public static Set<InetAddress> getAddresses() {
        Set<InetAddress> allAddresses = new LinkedHashSet();
        Map<String, Set<InetAddress>> interfaceAddressMap = getNetworkInterfaceAddresses();
        Iterator var2 = interfaceAddressMap.entrySet().iterator();

        while (true) {
            Set addresses;
            do {
                if (!var2.hasNext()) {
                    return allAddresses;
                }

                Map.Entry<String, Set<InetAddress>> entry = (Map.Entry) var2.next();
                addresses = entry.getValue();
            } while (addresses.isEmpty());

            Iterator var5 = addresses.iterator();

            while (var5.hasNext()) {
                InetAddress address = (InetAddress) var5.next();
                allAddresses.add(address);
            }
        }
    }

    public static Map<String, Set<InetAddress>> getNetworkInterfaceAddresses() {
        TreeMap interfaceAddressMap = new TreeMap();

        try {
            Enumeration ifaces = NetworkInterface.getNetworkInterfaces();

            while (true) {
                NetworkInterface iface;
                do {
                    do {
                        do {
                            if (!ifaces.hasMoreElements()) {
                                return interfaceAddressMap;
                            }

                            iface = (NetworkInterface) ifaces.nextElement();
                        } while (!iface.isUp());
                    } while (iface.isLoopback());
                } while (iface.isPointToPoint());

                String name = iface.getName();
                Enumeration ifaceAdresses = iface.getInetAddresses();

                while (ifaceAdresses.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ifaceAdresses.nextElement();
                    if (!ia.isLoopbackAddress() && !ia.getHostAddress().contains(":")) {
                        Set<InetAddress> addresses = (Set) interfaceAddressMap.get(name);
                        if (addresses == null) {
                            addresses = new LinkedHashSet();
                        }

                        ((Set) addresses).add(ia);
                        interfaceAddressMap.put(name, addresses);
                    }
                }
            }
        } catch (SocketException var7) {
            return interfaceAddressMap;
        }
    }

    public static String getLocalHostName() throws UnknownHostException {
        return chooseAddress().getHostName();
    }

    public static String getLocalIp() throws UnknownHostException {
        return chooseAddress().getHostAddress();
    }

    public static String getEnv(String envKey) {
        if (null != envKey && !"".equals(envKey)) {
            Map<String, String> map = System.getenv();
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
                String key = itr.next();
                if (envKey.equals(key)) {
                    return map.get(key);
                }
            }
        }

        return "";
    }
}
