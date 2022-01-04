package org.springblade.common.resubmit.annotion;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReSubmit {

    /**
     * 是否重复提交
     *
     * @return true
     */
    boolean isValidate() default true;
}
