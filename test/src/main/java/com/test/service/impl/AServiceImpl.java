package com.test.service.impl;

import __package__.common.mybatisplus.reference.sql.ReferenceServiceImpl;
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
public class AServiceImpl extends ReferenceServiceImpl<AMapper, A> implements AService, CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            boolean b = saveWithCheckReference(new A() {{
                setId(3);
                setTest("1q2dc1");
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
