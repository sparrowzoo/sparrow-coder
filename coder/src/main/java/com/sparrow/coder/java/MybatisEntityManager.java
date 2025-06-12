package com.sparrow.coder.java;

import com.sparrow.coder.config.MybatisMethodConfig;
import com.sparrow.orm.AbstractEntityManagerAdapter;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import java.util.Properties;
import javax.persistence.GenerationType;

public class MybatisEntityManager extends AbstractEntityManagerAdapter {
    private StringBuilder xml;
    private EnvironmentContext environmentContext;

    public String getXml() {
        return xml.toString();
    }

    public MybatisEntityManager(Class clazz, EnvironmentContext environmentContext) {
        super(clazz);
        this.clazz = clazz;
        this.environmentContext = environmentContext;
    }

    @Override
    public void init() {
        this.xml = new StringBuilder();
        Properties mybatisConfig = null;
        try {
            mybatisConfig = this.environmentContext.getMybatisConfig();
        } catch (IOException e) {
            logger.error("mybatis config {}", mybatisConfig);
            return;
        }
        this.generateHeader();
        this.generateResultMap();
        this.generateFieldSql();
        if (this.primary.getGenerationType().equals(GenerationType.AUTO)) {
            this.generateUuidInsert();
        } else {
            this.generateInsert();
        }
        this.generateUpdate();
        this.generateDelete();
        this.generateGetEntity();
        if ("true".equalsIgnoreCase(mybatisConfig.getProperty(MybatisMethodConfig.CHANGE_STATUS))) {
            String parameterType = mybatisConfig.getProperty(MybatisMethodConfig.PARAMETER_TYPE);
            if (StringUtility.isNullOrEmpty(parameterType)) {
                parameterType = "com.sparrow.protocol.dao.StatusCriteria";
            }

            String modifiedCondition = mybatisConfig.getProperty(MybatisMethodConfig.MODIFIED_CONDITION);

            if (StringUtility.isNullOrEmpty(modifiedCondition)) {
                modifiedCondition =
                    "`modified_user_name`=#{modifiedUserName},\n" +
                        " `modified_user_id`=#{modifiedUserId},\n" +
                        " `gmt_modified`=#{gmtModified}";
            }

            this.generateChangeStatus(parameterType, modifiedCondition);
        }
        xml.append("</mapper>");
    }

    @Override
    public String parsePropertyParameter(String column, String property) {
        return "#{" + property + "}";
    }

    private void generateHeader() {
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org/DTD Mapper 3.0\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        String[] clazzNameArray = this.className.split("\\.");
        if (clazzNameArray.length < 2) {
            throw new RuntimeException("package split length must be great then 2 e.g com.sparrow.spring.po.Code");
        }
        xml.append("<mapper namespace=\"$package_dao.$class_dao\">\n");
    }

    private void generateResultMap() {
        xml.append(String.format("<resultMap id=\"%sMap\" type=\"%s\">\n", this.simpleClassName, this.className));
        xml.append(String.format("<id column=\"%s\" property=\"%s\" />\n", this.primary.getColumnName(), this.primary.getPropertyName()));
        for (String column : this.columnPropertyMap.keySet()) {
            if (column.equals(this.primary.getColumnName())) {
                continue;
            }
            xml.append(String.format(" <result column=\"%s\" property=\"%s\"/>\n", column, this.columnPropertyMap.get(column)));
        }
        xml.append("</resultMap>\n");
    }

    private void generateGetEntityByUnique() {
        xml.append(String.format("<select id=\"getEntityByUnique\" resultMap=\"%sMap\" parameterType=\"com.sparrow.support.db.UniqueKeyCriteria\">\n", this.simpleClassName));
        xml.append(" SELECT \n");
        xml.append("<include refid=\"fields\"/>\n");
        xml.append(" FROM \n");
        xml.append(this.tableName);
        xml.append(" WHERE ${uniqueFieldName}= #{key}\n");
        xml.append("</select>\n");
    }

    private void generateChangeStatus(String parameterType, String modifiedCondition) {
        xml.append("<update id=\"changeStatus\" parameterType=\"").append(parameterType).append("\">\n");
        xml.append(String.format("update `%1$s` set `%2$s`=#{status},\n", this.tableName, this.status.getColumnName()));
        xml.append(modifiedCondition);
        xml.append(" WHERE id IN\n");
        xml.append("<foreach collection=\"idArray\" item=\"id\" index=\"index\" open=\"(\" close=\")\" separator=\",\">\n");
        xml.append(String.format("#{%s}", this.primary.getColumnName()));
        xml.append("</foreach>\n");
        xml.append("</update>");
    }

    private void generateGetEntity() {
        xml.append(String.format("<select id=\"getEntity\" resultMap=\"%sMap\" parameterType=\"%s\">\n", this.simpleClassName, this.primary.getType().getName()));
        xml.append(" SELECT \n");
        xml.append("<include refid=\"fields\"/>\n");
        xml.append(" FROM \n");
        xml.append(this.tableName);
        xml.append(String.format("\nWHERE %s= #{%s}\n", this.primary.getColumnName(), this.primary.getPropertyName()));
        xml.append("</select>\n");
    }

    private void generateFieldSql() {
        xml.append(String.format("<sql id=\"fields\">\n%s\n</sql>\n", this.fields));
    }

    private void generateInsert() {

        String autoGenerateConfig = "";
        if (this.primary.getGenerationType().equals(GenerationType.IDENTITY)) {
            autoGenerateConfig = String.format(" useGeneratedKeys=\"true\" keyColumn=\"%1$s\" keyProperty=\"%2$s\"", this.primary.getColumnName(), this.primary.getPropertyName());
        }

        xml.append(String.format("<insert id=\"insert\" parameterType=\"%1$s\" %2$s>\n", this.className,autoGenerateConfig));
        xml.append(this.insert);
        xml.append("</insert>");
    }

    private void generateUuidInsert() {
        xml.append(String.format("<insert id=\"insert\" parameterType=\"%s\">\n", this.className));
        xml.append(String.format("<selectKey keyProperty=\"%s\" keyColumn=\"%s\"  order=\"BEFORE\" resultType=\"java.lang.String\">\n", this.primary.getPropertyName(), this.primary.getColumnName()));
        xml.append("select UUID()\n");
        xml.append("</selectKey>\n");
        xml.append(this.insert);
        xml.append("\n</insert>\n");
    }

    private void generateUpdate() {
        xml.append(String.format(" \n<update id=\"update\" parameterType=\"%s\">\n", this.className));
        xml.append(this.update);
        xml.append("\n</update>\n");
    }

    private void generateDelete() {
        xml.append(String.format("<delete id=\"delete\" parameterType=\"%s\">\n", this.primary.getType().getName()));
        xml.append(this.delete);
        xml.append("\n</delete>\n");
    }
}
