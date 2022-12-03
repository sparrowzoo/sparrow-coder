package com.sparrow.coding.validating.po;

import com.sparrow.protocol.MethodOrder;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Forum {
    private Long forumId;
    /**
     * 资源 ID
     */
    private Long resourceId;
    /**
     * 版本前台列表对应的url
     */
    private String listUrl;
    /**
     * 版本后台管理新建对应的url
     */
    private String newUrl;
    /**
     * 版本前台展示详情页对应url
     */
    private String detailUrl;
    /**
     * cms-{100}.do 访问的url placeholder
     * <p>
     * cms-manage.do?forumCode=100
     */
    private String placeholderUrl;
    /**
     * 上传的key 由文件上传服务提供
     */
    private String uploadKey;
    /**
     * 版块的封面图
     */
    private String cover;
    /**
     * 版本的管理员
     */
    private String manager;
    /**
     * 版块最大的记录数
     */
    private Integer maxRecordCount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    @MethodOrder(order = 1)
    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    @MethodOrder(order = 2)
    @Column(name = "resource_id", columnDefinition = "int(11)  DEFAULT 0 COMMENT 'resource id'", nullable = false)
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @MethodOrder(order = 3)
    @Column(name = "list_url", columnDefinition = "varchar(256)  DEFAULT '' COMMENT 'list url'", nullable = false)
    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    @MethodOrder(order = 4)
    @Column(name = "detail_url", columnDefinition = "varchar(256)  DEFAULT '' COMMENT 'detail url'", nullable = false)
    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @MethodOrder(order = 5F)
    @Column(name = "placeholder_url", columnDefinition = "varchar(16)  DEFAULT '' COMMENT 'placeholder url'", nullable = false, unique = true)
    public String getPlaceholderUrl() {
        return this.placeholderUrl;
    }

    public void setPlaceholderUrl(String placeholderUrl) {
        this.placeholderUrl = placeholderUrl;
    }

    @MethodOrder(order = 6F)
    @Column(name = "new_url", columnDefinition = "varchar(256)  DEFAULT '' COMMENT 'new url'", nullable = false)
    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    @MethodOrder(order = 7)
    @Column(name = "manager", columnDefinition = "varchar(64)  DEFAULT '' COMMENT 'manager url'", nullable = false)
    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @MethodOrder(order = 8)
    @Column(name = "upload_key", columnDefinition = "varchar(64)  DEFAULT '' COMMENT 'upload key'", nullable = false)
    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    @Column(name = "cover", columnDefinition = "varchar(256)  DEFAULT '' COMMENT 'cover'", nullable = false)
    @MethodOrder(order = 9F)
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @MethodOrder(order = 10)
    @Column(name = "max_record_count", columnDefinition = "int(11)  DEFAULT 0 COMMENT 'max record count'", nullable = false)
    public Integer getMaxRecordCount() {
        return maxRecordCount;
    }

    public void setMaxRecordCount(Integer maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
    }
}
