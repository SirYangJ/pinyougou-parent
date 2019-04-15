package com.itheima.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.DemoInterface;
import com.pinyougou.mapper.TbAddressMapper;
import com.pinyougou.pojo.TbAddress;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yangjie
 * @create 2019-03-26 22:58
 */
@Service
public class DemoImpl implements DemoInterface {
    @Autowired
    private TbAddressMapper tbAddressMapper;


    @Override
    public List<TbAddress> findAll() {
        return tbAddressMapper.selectByExample(null);
    }

}
