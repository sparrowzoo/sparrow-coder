package com.sparrow.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.orm.Field;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import com.sparrow.coder.utils.JavaTsTypeConverter;
import jakarta.inject.Named;

import java.util.Map;

@Named
public class TSClassPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_class.name(), this.generateClass(tableContext));
    }

    private String generateClass(TableContext tableContext) {
        Map<String, Field> fields = tableContext.getEntityManager().getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("%s:%s; \n", field.getPropertyName(), JavaTsTypeConverter.toTsType(fieldClazz));
            fieldBuild.append(property);
        }
        String className = tableContext.getTableConfig().getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        return String.format("export interface %1$s extends BasicData<%1$s> \n{\n %2$s\n}", className, fieldBuild);
    }
}
