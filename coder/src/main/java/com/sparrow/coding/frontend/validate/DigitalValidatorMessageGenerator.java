package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.DigitalValidator;
import java.util.Map;
import javax.inject.Named;

@Named
public class DigitalValidatorMessageGenerator extends AbstractValidatorMessageGenerator<DigitalValidator> {
    @Override protected void appendDefaultValue(StringBuilder sb, Map<String, Object> maps) {
        Object defaultValue = maps.get(DEFAULT_VALUE);
        if (defaultValue != null && defaultValue.equals(Integer.MIN_VALUE)) {
            sb.append(this.formatMessage("defaultValue", defaultValue));
        }
    }

    @Override public Class<DigitalValidator> getValidateAnnotation() {
        return DigitalValidator.class;
    }
}
