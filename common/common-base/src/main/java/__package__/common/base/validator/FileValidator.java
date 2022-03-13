package __package__.common.base.validator;

import __package__.common.base.validator.annotation.FileNotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Mr_W
 * @date 2021/4/10 15:39
 * @description 文件检验
 */
public class FileValidator implements ConstraintValidator<FileNotEmpty, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // 检查文件是否为空(允许不传文件，不允许文件为空)
        return Objects.isNull(value) || !value.isEmpty();
    }

}
