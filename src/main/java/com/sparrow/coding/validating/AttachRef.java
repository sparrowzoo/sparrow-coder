package com.sparrow.coding.validating;

import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.POJO;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class AttachRef implements POJO {

    /**
     * 引用状态
     */
    private Long id;

    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 文件所属对象类别
     */
    private String belongType;
    /**
     * 文件所属对象id
     */
    private Long belongId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

    private Long createTime;

    private Long updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    @MethodOrder(order = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(order = 2)
    @Column(name = "file_id", columnDefinition = "int(11) UNSIGNED DEFAULT 0 COMMENT 'FILE ID'", nullable = false, updatable = false)
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @MethodOrder(order = 3)
    @Column(name = "belong_type", columnDefinition = "varchar(64) DEFAULT '' COMMENT '所属业务'", nullable = false)
    public String getBelongType() {
        return belongType;
    }

    public void setBelongType(String belongType) {
        this.belongType = belongType;
    }


    @MethodOrder(order = 4)
    @Column(name = "business_id", columnDefinition = "int(10) UNSIGNED     COMMENT 'business id'", nullable = false)
    public Long getBelongId() {
        return belongId;
    }

    public void setBelongId(Long belongId) {
        this.belongId = belongId;
    }

    @MethodOrder(order = 5)
    @Column(name = "status", columnDefinition = "tinyint(1) DEFAULT 0 COMMENT 'status'", nullable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @MethodOrder(order = 6)
    @Column(name = "remarks", columnDefinition = "varchar(512) DEFAULT '' COMMENT '备注'", nullable = false)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @MethodOrder(order = 7)
    @Column(name = "create_time", columnDefinition = "bigint(20) DEFAULT 0", nullable = false, updatable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @MethodOrder(order = 8)
    @Column(name = "update_time", columnDefinition = "bigint(20) DEFAULT 0", nullable = false)
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
