package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.constant.Config;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import com.sparrowzoo.coder.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class DefaultClassArchAccessor implements ClassArchAccessor {
    protected final TableConfigRegistry registry;
    protected final TableContext tableContext;
    protected final Properties config;
    protected final FileUtility fileUtility = FileUtility.getInstance();

    public DefaultClassArchAccessor(TableConfigRegistry registry, String tableName) throws IOException {
        this.registry = registry;
        this.config = ConfigUtils.initPropertyConfig(registry.getProject().getConfig());
        this.tableContext=registry.getTableContext(tableName);
    }

    @Override
    public String getPackage(ClassKey classKey) {
        String packageName = this.config.getProperty(Config.PACKAGE_PREFIX + classKey.name().toLowerCase());
        if (packageName == null) {
            return "";
        }
        if (!registry.getProject().getWrapWithParent()) {
            packageName = packageName.replaceAll("admin.", "");
        }
        String poPackage =this.tableContext.getPoPackage();
        return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageName, Symbol.DOT);
    }


    public String getClassName(ClassKey classKey) {
        String originClassName = config.getProperty(Config.CLASS_PREFIX + classKey.name().toLowerCase());
        String persistenceClassName = this.tableContext.getPlaceHolder().get(PlaceholderKey.$persistence_class_name.name());
        return originClassName.replace(PlaceholderKey.$persistence_class_name.name(),persistenceClassName);
    }

    public String getModule(ClassKey key) {
        String parentModule = "admin";
        String moduleKey = Config.MODULE_PREFIX;
        if (!ClassKey.PO.equals(key)) {
            moduleKey += parentModule + "." + key.getModule().toLowerCase();
        } else {
            moduleKey += key.getModule().toLowerCase();
        }
        String modulePath = config.getProperty(moduleKey);
        if (modulePath == null) {
            log.error("module path is null, module key is [{}]", moduleKey);
            return "";
        }
        return modulePath.replace(PlaceholderKey.$module_prefix.name(),registry.getProject().getModulePrefix());
    }
}
