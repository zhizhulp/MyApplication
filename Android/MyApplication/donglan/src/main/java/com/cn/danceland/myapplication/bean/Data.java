/**
 * Copyright 2017 bejson.com
 */
package com.cn.danceland.myapplication.bean;


import java.io.Serializable;

/**
 * Auto-generated: 2017-11-07 13:20:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data implements Serializable {

    private String id;// 主键
    private String cName; // 中文名称 Varchar(50)
    private String phone; // 手机号 Varchar(20)
    private String nickName; // 昵称 Vharchar(50)
    private String height; // 身高 Float
    private String weight; // 体重 Float
    private String regDate; // 注册日期 Date
    private String birthday; // 出生日期
    private String zoneCode; // 区化编码
    private String enabled; // 是否启用
    private String auth; // 身份
    private String branchId; // 所属门店
    private String gender; // 用户性别
    private String status; // 在线状态
    private String memberNo; // 会员编号 Varchar(50)
    private String password;// 登录密码
    private String romType; // 平台
    private String userName; // 用户自定义的会员号 Varchar(50)
    private String avatarPath; // 头像物理路径 Varchar(200)
    private String selfAvatarPath; // 个性头像物理路径 Varchar(200)
    private String teachMumberId; // 指导教练
    private String awareWay; // 了解途径
    private String adminMumberId; // 所属会籍
    private String remark; // 备注

    private String follower;
    private String token;
    private String verCode;

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", cName='" + cName + '\'' +
                ", phone='" + phone + '\'' +
                ", nickName='" + nickName + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", regDate='" + regDate + '\'' +
                ", birthday='" + birthday + '\'' +
                ", zoneCode='" + zoneCode + '\'' +
                ", enabled='" + enabled + '\'' +
                ", auth='" + auth + '\'' +
                ", branchId='" + branchId + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", memberNo='" + memberNo + '\'' +
                ", password='" + password + '\'' +
                ", romType='" + romType + '\'' +
                ", userName='" + userName + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", selfAvatarPath='" + selfAvatarPath + '\'' +
                ", teachMumberId='" + teachMumberId + '\'' +
                ", awareWay='" + awareWay + '\'' +
                ", adminMumberId='" + adminMumberId + '\'' +
                ", remark='" + remark + '\'' +
                ", follower='" + follower + '\'' +
                ", token='" + token + '\'' +
                ", verCode='" + verCode + '\'' +
                '}';
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getcName() {

        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCName() {
        return cName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRomType(String romType) {
        this.romType = romType;
    }

    public String getRomType() {
        return romType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setSelfAvatarPath(String selfAvatarPath) {
        this.selfAvatarPath = selfAvatarPath;
    }

    public String getSelfAvatarPath() {
        return selfAvatarPath;
    }

    public void setTeachMumberId(String teachMumberId) {
        this.teachMumberId = teachMumberId;
    }

    public String getTeachMumberId() {
        return teachMumberId;
    }

    public void setAwareWay(String awareWay) {
        this.awareWay = awareWay;
    }

    public String getAwareWay() {
        return awareWay;
    }

    public void setAdminMumberId(String adminMumberId) {
        this.adminMumberId = adminMumberId;
    }

    public String getAdminMumberId() {
        return adminMumberId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollower() {
        return follower;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}