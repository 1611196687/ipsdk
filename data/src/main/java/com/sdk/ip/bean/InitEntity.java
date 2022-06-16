package com.sdk.ip.bean;

import java.io.Serializable;

public class InitEntity implements Serializable {


    /**
     * client_ip : 180.124.214.139
     * second : 86400
     * server_ip_list : 101.42.178.239,43.138.156.43,49.234.139.111
     * task_id : 1260
     */

    private String client_ip;
    private String second;
    private String server_ip_list;
    private Integer task_id;

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getServer_ip_list() {
        return server_ip_list;
    }

    public void setServer_ip_list(String server_ip_list) {
        this.server_ip_list = server_ip_list;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }
}
