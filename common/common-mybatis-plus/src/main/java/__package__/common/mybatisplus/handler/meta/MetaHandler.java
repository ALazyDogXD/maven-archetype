package __package__.common.mybatisplus.handler.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author Mr_W
 * @date 2021/4/10 17:06
 * @description 数据字段处理
 */
public class MetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println(metaObject.getOriginalObject());
        this.setFieldValByName("gmt_create", LocalDateTime.now(), metaObject);
        this.setFieldValByName("gmt_modified", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmt_modified", LocalDateTime.now(), metaObject);
    }
}
