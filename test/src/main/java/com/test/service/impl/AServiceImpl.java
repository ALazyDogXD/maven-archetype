package com.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.entity.A;
import com.test.mapper.AMapper;
import com.test.service.AService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ALazyDogXD
 * @date 2022/4/11 13:46
 * @description a
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AServiceImpl extends ServiceImpl<AMapper, A> implements AService, CommandLineRunner {

    @Override
    public void run(String... args) {
        save(new A() {{
            setId(3);
            setTest("1q2dc1");
        }});
    }
}
