package com.sdk.ip.bean;

public class IpAddEntity {

    private int task_id;
    private int sort_asc;
    private int sort_desc;
    private String original_ip;
    private String target_ip;
    private String trace_ip;
    private int create_time;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getSort_asc() {
        return sort_asc;
    }

    public void setSort_asc(int sort_asc) {
        this.sort_asc = sort_asc;
    }

    public int getSort_desc() {
        return sort_desc;
    }

    public void setSort_desc(int sort_desc) {
        this.sort_desc = sort_desc;
    }

    public String getOriginal_ip() {
        return original_ip;
    }

    public void setOriginal_ip(String original_ip) {
        this.original_ip = original_ip;
    }

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    public String getTrace_ip() {
        return trace_ip;
    }

    public void setTrace_ip(String trace_ip) {
        this.trace_ip = trace_ip;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }
}
