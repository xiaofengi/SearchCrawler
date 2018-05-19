package org.crawler.mysql.model;

import java.util.Date;

public class ProxyEntity {
    private Integer id;

    private String host;

    private Integer port;

    private String location;

    private String type;

    private String anonymousType;

    private String resTime;

    private Boolean isUsing;

    private Boolean enable;

    private Date createTime;

    private Date lastUseTime;

    public ProxyEntity() {
    }

    public ProxyEntity(String host, Integer port, String location, String type, String anonymousType, String resTime, Boolean isUsing, Boolean enable, Date createTime) {
        this.host = host;
        this.port = port;
        this.location = location;
        this.type = type;
        this.anonymousType = anonymousType;
        this.resTime = resTime;
        this.isUsing = isUsing;
        this.enable = enable;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAnonymousType() {
        return anonymousType;
    }

    public void setAnonymousType(String anonymousType) {
        this.anonymousType = anonymousType == null ? null : anonymousType.trim();
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = resTime == null ? null : resTime.trim();
    }

    public Boolean getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Boolean isUsing) {
        this.isUsing = isUsing;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
}