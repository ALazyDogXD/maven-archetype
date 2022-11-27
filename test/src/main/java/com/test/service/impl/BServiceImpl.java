package com.test.service.impl;

import __package__.common.mybatisplus.reference.sql.ReferenceServiceImpl;
import com.test.entity.B;
import com.test.mapper.BMapper;
import com.test.service.BService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ALazyDogXD
 * @date 2022/4/12 21:04
 * @description b
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class BServiceImpl extends ReferenceServiceImpl<BMapper, B> implements BService {
}
