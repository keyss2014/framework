package cn.keyss.common.utilities;

import java.net.InetAddress;

/**
 * Created by zhangyouling on 2015/10/20.
 */
abstract public class HostHelper {

    /**
     * 取本机名称
     */
    public static String getLocalHostName(){
        try{
            InetAddress addr= InetAddress.getLocalHost();
            return addr.getHostName();
        }catch(Exception e){
            return "";
        }
    }

    /**
     * 取内网IP
     */
    public static String getLocalHostIP(){
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        }catch (Exception e){
            return "";
        }
    }

}
