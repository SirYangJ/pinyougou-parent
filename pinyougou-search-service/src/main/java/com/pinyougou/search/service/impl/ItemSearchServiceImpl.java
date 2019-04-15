package com.pinyougou.search.service.impl;

import java.util.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {
	@Autowired
	private SolrTemplate solrTemplate;

	@Override
	public Map search(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		map.putAll(highlightList(searchMap));
		map.putAll(categoryList(searchMap));
		return map;
	}

	/**
	 * 设置高亮
	 */
	private Map highlightList(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		// 设置高亮
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// 设置高亮域
		highlightOptions.setSimplePrefix("<em style='color:red'>");
		highlightOptions.setSimplePostfix("</em>");
		query.setHighlightOptions(highlightOptions);
		// 查询条件
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
		List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
		for (HighlightEntry<TbItem> highlightEntry : highlighted) {
			TbItem item = highlightEntry.getEntity();
			if (highlightEntry.getHighlights().size() >= 0
					&& highlightEntry.getHighlights().get(0).getSnipplets().size() >= 0) {
				item.setTitle(highlightEntry.getHighlights().get(0).getSnipplets().get(0));
			}
		}
		map.put("rows", highlightPage.getContent());
		return map;
	}


	/**
	 * 获得查询商品的的分类
	 */
	private Map categoryList(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		List<String> list=new ArrayList<String>();
		Query query=new SimpleQuery("*:*");
		//设置查询条件
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//设置分组
		GroupOptions groupOptions=new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		for (GroupEntry<TbItem> groupEntry : groupEntries) {
			list.add(groupEntry.getGroupValue());
		}
		map.put("categoryList", list);
		return map;

	}
	@Test
	public void aaa() {
		String[] array = new String[]{};
		System.out.println(array.length);
	}
}
