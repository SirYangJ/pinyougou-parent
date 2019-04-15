package com.itheima.web;

import com.itheima.DemoInterface;
import com.pinyougou.pojo.TbAddress;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjie
 * @create 2019-03-26 23:05
 */
@RestController
@RequestMapping("/demo")
public class DemoWeb {
    @Reference
    private DemoInterface demoInterface;
    @RequestMapping("/findAll")
    public List<TbAddress> findAll(){
        List<TbAddress> list=demoInterface.findAll();
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println(list);
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        System.out.println("----------------");
        return list;
    }
}
