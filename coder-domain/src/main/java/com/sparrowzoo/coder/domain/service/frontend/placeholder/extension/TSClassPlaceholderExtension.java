package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.orm.Field;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class TSClassPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext) {
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_class.name(), this.generateClass(tableContext));
    }

    private String generateClass(TableContext tableContext) {
        Map<String, Field> fields = tableContext.getEntityManager().getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("%s:%s; \n", field.getPropertyName(), this.toType(fieldClazz));
            fieldBuild.append(property);
        }
        String className = tableContext.getTableConfig().getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        return String.format("export interface %1$s extends BasicData<%1$s> \n{\n %2$s\n}", className, fieldBuild);
    }

    private String toType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return "number";
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return "boolean";
        }
        return "string";
    }
}
