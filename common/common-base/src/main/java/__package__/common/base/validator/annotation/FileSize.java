package __package__.common.base.validator.annotation;

import __package__.common.base.validator.FileSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Mr_W
 * @date 2021/4/10 15:51
 * @description 文件大小
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy ={FileSizeValidator.class})
public @interface FileSize {

    // 图片大小限制 (byte)
    long max() default 1 << 20;

    // 当验证不通过时的提示信息
    String message() default "文件过大";

    // 约束注解在验证时所属的组别
    Class<?>[] groups() default { };

    // 负载
    Class<? extends Payload>[] payload() default { };

}
