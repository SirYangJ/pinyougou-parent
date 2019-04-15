package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	public void importItemData() {
		
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		List<TbItem> list = itemMapper.selectByExample(example);
		for (TbItem item:list) {
			System.out.println(item.getTitle());
			Map map = JSON.parseObject(item.getSpec());
			item.setSpecMap(map);
		}
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	public static void main(String[] args) {
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext
				("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil=(SolrUtil)applicationContext.getBean("solrUtil");
		solrUtil.importItemData();
	}
}
