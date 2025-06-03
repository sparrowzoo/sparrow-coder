package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.DigitalCategory;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.coding.protocol.validate.StringValidator;

import java.util.Map;
import javax.inject.Named;

@Named
public class DigitalValidatorMessageGenerator extends AbstractValidatorMessageGenerator<DigitalValidator> {
    @Override
    public String generateValidateMessage(String propertyName, DigitalValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.transform(validator.getCategory()));
        pipeline.append(this.number());
        pipeline.append(this.minValue(validator));
        pipeline.append(this.maxValue(validator));
        this.finish(pipeline);
        if (validator.isAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
