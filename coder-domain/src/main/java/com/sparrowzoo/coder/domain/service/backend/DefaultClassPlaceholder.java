package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.constant.Config;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class DefaultClassPlaceholder implements ClassPlaceholderGenerator {
    protected final ProjectBO project;
    protected final TableContext tableContext;
    protected final Properties scaffoldConfig;
    protected final FileUtility fileUtility = FileUtility.getInstance();

    public DefaultClassPlaceholder(ProjectBO project,TableContext tableContext){
        this.project = project;
        this.scaffoldConfig = project.getScaffoldConfig();
        this.tableContext =tableContext;
    }

    public String getModule(ClassKey key) {
        String parentModule = "admin";
        String moduleKey = Config.MODULE_PREFIX;
        if (!ClassKey.PO.equals(key)) {
            moduleKey += parentModule + "." + key.getModule().toLowerCase();
        } else {
            moduleKey += key.getModule().toLowerCase();
        }
        String modulePath = this.scaffoldConfig.getProperty(moduleKey);
        if (modulePath == null) {
            log.error("module path is null, module key is [{}]", moduleKey);
            return "";
        }
        return modulePath.replace(PlaceholderKey.$module_prefix.name(), this.project.getProjectConfig().getModulePrefix());
    }
    @Override
    public String getPackage(ClassKey classKey) {
        String packageName = this.scaffoldConfig.getProperty(Config.PACKAGE_PREFIX + classKey.name().toLowerCase());
        if (packageName == null) {
            return "";
        }
        if (!this.project.getProjectConfig().getWrapWithParent()) {
            packageName = packageName.replaceAll("admin.", "");
        }
        String poPackage = this.tableContext.getPoPackage();
        return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageName, Symbol.DOT);
    }


    public String getClassName(ClassKey classKey) {
        String originClassName = this.scaffoldConfig.getProperty(Config.CLASS_PREFIX + classKey.name().toLowerCase());
        String persistenceClassName =tableContext.getEntityManager().getSimpleClassName();
        return originClassName.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
    }
}
