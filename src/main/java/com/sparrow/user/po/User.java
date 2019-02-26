package com.sparrow.user.po;

import com.sparrow.constant.CONFIG;
import com.sparrow.support.Entity;
import com.sparrow.utility.Config;
import com.sparrow.utility.StringUtility;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User implements Entity, Cloneable {
    /*-------基本信息-------------*/
    private Long userId;
    private String userName;
    private String nickName;
    private String password;
    private String avatar;
    private String sex;
    private Date birthday;
    private String email;
    private String mobile;
    private Long cent;
    private String activate;
    private Long activateTime;
    private Long createTime;
    private Long updateTime;
    private Long lastLoginTime;
    private String website;
    private String ip;
    //设备
    private String device;
    private String deviceId;
    private String deviceModel;
    //referer
    private String origin;
    private String isOnline;
    private String personalSignature;
    //是否可用enable disable
    private String status;

    private String zone;

    private String groupLevel;

    private String secretMobile;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name = "email", unique = true, updatable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "user_name", updatable = false, unique = true)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password", updatable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "avatar", updatable = false)
    public String getAvatar() {
        if (StringUtility.isNullOrEmpty(this.avatar)) {
            this.avatar = Config.getValue(CONFIG.DEFAULT_USER_HEAD);
        }
        return avatar;
    }

    public void setAvatar(String headImg) {
        this.avatar = headImg;
    }

    @Column(name = "personal_signature")
    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "mobile", updatable = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "create_time", updatable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "last_login_time", updatable = false)
    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Column(name = "cent", updatable = false)
    public Long getCent() {
        return cent;
    }

    public void setCent(Long cent) {
        this.cent = cent;
    }

    @Column(name = "status", updatable = false)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "ip", updatable = false)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "is_online", updatable = false)
    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    @Column(name = "activate", updatable = false)
    public String getActivate() {
        return this.activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    @Column(name = "activate_time", updatable = false)
    public Long getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Long activeTime) {
        this.activateTime = activeTime;
    }

    @Column(name = "zone", updatable = false)
    public String getZone() {
        return zone;
    }

    @Column(name = "origin", updatable = false)
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Column(name = "website", updatable = false)
    public String getWebsite() {
        if (StringUtility.isNullOrEmpty(website)) {
            website = Config.getValue(CONFIG.WEBSITE);
        }
        return website;
    }

    @Column(name = "group_level", updatable = false)
    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    @Column(name = "secret_mobile", updatable = false)
    public String getSecretMobile() {
        return secretMobile;
    }

    public void setSecretMobile(String secretMobile) {
        this.secretMobile = secretMobile;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Column(name = "device", updatable = false)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Column(name = "device_id", updatable = false)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "device_model", updatable = false)
    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}
