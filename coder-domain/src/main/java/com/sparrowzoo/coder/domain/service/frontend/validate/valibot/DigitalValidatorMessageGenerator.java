package com.sparrowzoo.coder.domain.service.frontend.validate.valibot;

import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;

import javax.inject.Named;

@Named
public class DigitalValidatorMessageGenerator extends AbstractValidatorMessageGenerator<DigitalValidator> {
    @Override
    public String outerGenerateMessage(String propertyName, DigitalValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.check(validator, validator.getCategory().getRegex(), validator.getDigitalMessage()));
        pipeline.append(this.transform(validator.getCategory()));
        pipeline.append(this.minValue(validator));
        pipeline.append(this.maxValue(validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
