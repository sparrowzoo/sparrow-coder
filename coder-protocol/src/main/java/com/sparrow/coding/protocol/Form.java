package com.sparrow.coding.protocol;

import com.sparrow.coding.protocol.validate.NullValidator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Form {
    boolean primaryKey() default false;

    String text();

    boolean showInEdit() default true;

    boolean showInList() default false;

    int width() default 80;

    ControlType type() default ControlType.INPUT_TEXT;

    ControlType listType() default ControlType.LABEL;

    Class<? extends Annotation> validate() default NullValidator.class;
}
