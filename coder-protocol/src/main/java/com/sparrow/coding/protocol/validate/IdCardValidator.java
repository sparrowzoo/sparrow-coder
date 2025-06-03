package com.sparrow.coding.protocol.validate;

import lombok.Data;

@Data
public class IdCardValidator extends RegexValidator {
    protected int minLength = 15;
    protected int maxLength = 18;
}
