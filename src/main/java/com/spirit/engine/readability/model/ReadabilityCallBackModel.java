package com.spirit.engine.readability.model;

import java.util.HashMap;
import java.util.Map;

public class ReadabilityCallBackModel {

    private String aid;
    private String uid;
    private String pids;
    private String text;
    private long ctime;

    public ReadabilityCallBackModel(){

    }

    public void setAid(String aid){
        this.aid = aid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("aid=").append(this.aid);
        sb.append("&uid=").append(this.uid);
        sb.append("&text=").append(this.text);
        sb.append("&pids=").append(this.pids);
        sb.append("&time=").append(this.ctime);
        return sb.toString();
    }

    public Map<String, String> toParams(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("aid", aid);
        map.put("uid", uid);
        map.put("text", text);
        map.put("pids", pids);
        map.put("time", String.valueOf(ctime));
        return map;
    }
}
