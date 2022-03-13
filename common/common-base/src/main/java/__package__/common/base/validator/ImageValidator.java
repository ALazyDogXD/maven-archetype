package __package__.common.base.validator;

import __package__.common.base.validator.annotation.IsImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Mr_W
 * @date 2021/4/10 14:22
 * @description 图片检验
 */
public class ImageValidator implements ConstraintValidator<IsImage, MultipartFile> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageValidator.class);

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        // 检查是否是图片
        try (InputStream in = value.getInputStream()) {
            if (Objects.isNull(ImageIO.read(in))) {
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("文件读取失败", e);
            return false;
        }
        return true;
    }


}
