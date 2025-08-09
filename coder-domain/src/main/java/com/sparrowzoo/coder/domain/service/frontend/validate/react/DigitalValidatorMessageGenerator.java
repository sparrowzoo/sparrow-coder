package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;

import javax.inject.Named;

@Named
public class DigitalValidatorMessageGenerator extends AbstractValidatorMessageGenerator<DigitalValidator> {
    @Override
    public String outerGenerateMessage(String propertyName, DigitalValidator validator) {
        if(StringUtility.isNullOrEmpty(validator.getDigitalMessage())){
            validator.setDigitalMessage(this.defaultValidator.getDigitalMessage());
        }
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        pipeline.append(this.check(propertyName,validator, validator.getCategory().getRegex(), validator.getDigitalMessage()));
        pipeline.append(this.transform(validator.getCategory()));
        pipeline.append(this.minValue(propertyName,validator));
        pipeline.append(this.maxValue(propertyName,validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }


    @Override
    public DigitalValidator defaultValidator() {
        return DigitalValidator.DIGITAL_VALIDATOR;
    }
}
