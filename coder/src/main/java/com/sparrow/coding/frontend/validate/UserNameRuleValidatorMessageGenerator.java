package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.UserNameRuleValidator;
import javax.inject.Named;

@Named
public class UserNameRuleValidatorMessageGenerator extends AbstractValidatorMessageGenerator<UserNameRuleValidator> {

    @Override public Class<UserNameRuleValidator> getValidateAnnotation() {
        return UserNameRuleValidator.class;
    }
}
