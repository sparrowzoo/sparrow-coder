package com.sparrow.example.po;

import com.sparrow.protocol.FieldOrder;
import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.dao.PO;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sparrow_example")
public class SparrowExample extends PO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11)")
    @FieldOrder(order = 0)
    private Long id;

    private String avatar;

    private String userName;

    private String password;

    private String age;

    private String email;

    private String confirmPassword;

    private String idCard;

    private String mobile;

    private String tel;

    private String name;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(order = 1)
    @Column(name = "avatar", columnDefinition = "varchar(256) DEFAULT '' COMMENT '头象'", updatable = false)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @MethodOrder(order = 2)
    @Column(name = "user_name", columnDefinition = "varchar(32) DEFAULT '' COMMENT '用户名'", updatable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @MethodOrder(order = 3)
    @Column(name = "password", columnDefinition = "varchar(32) DEFAULT '' COMMENT '密码'", updatable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @MethodOrder(order = 3)
    @Column(name = "age", columnDefinition = "int(10) UNSIGNED DEFAULT 0 COMMENT '年龄'", updatable = false)
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @MethodOrder(order = 4)
    @Column(name = "email", columnDefinition = "varchar(256) DEFAULT '' COMMENT 'email'", updatable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @MethodOrder(order = 4)
    @Column(name = "confirm_password", columnDefinition = "varchar(32) DEFAULT '' COMMENT '确认密码'", updatable = false)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @MethodOrder(order = 5)
    @Column(name = "id_card", columnDefinition = "varchar(32) DEFAULT '' COMMENT '身份证'", updatable = false)
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @MethodOrder(order = 5)
    @Column(name = "mobile", columnDefinition = "varchar(16) DEFAULT '' COMMENT '手机号'", updatable = false)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @MethodOrder(order = 5)
    @Column(name = "tel", columnDefinition = "varchar(16) DEFAULT '' COMMENT '联系电话'", updatable = false)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @MethodOrder(order = 5)
    @Column(name = "name", columnDefinition = "varchar(16) DEFAULT '' COMMENT '用户姓名'", updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
