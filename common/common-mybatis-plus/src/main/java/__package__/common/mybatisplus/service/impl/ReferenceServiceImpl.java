package __package__.common.mybatisplus.service.impl;

import __package__.common.mybatisplus.mapper.ReferenceMapper;
import __package__.common.mybatisplus.service.ReferenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author alazydogxd
 * @date 2022/5/26 12:33 AM
 * @description 外键约束服务
 */
public class ReferenceServiceImpl<M extends ReferenceMapper<E>, E> extends ServiceImpl<M, E> implements ReferenceService<E> {

    @Autowired
    protected M referenceMapper;

    @Override
    public ReferenceMapper<E> getReferenceMapper() {
        return referenceMapper;
    }
}
