package org.crawler.mysql.mapper;

import java.util.List;
import java.util.Set;

import org.crawler.mysql.model.Keyword;

public interface KeywordMapper {
	public Set<String> selectKeywordByCat(Long catId);

	public void batchInsert(List<Keyword> list);
}
