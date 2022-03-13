package __package__.common.base.validator;

import __package__.common.base.validator.annotation.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Mr_W
 * @date 2021/4/10 15:51
 * @description 文件大小
 */
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private long max;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        // 检查文件大小
        return value.getSize() <= max;
    }

}
