package org.crawler.mysql.mapper;

import org.crawler.mysql.model.WebPageDetail;

public interface WebPageDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WebPageDetail record);

    int insertSelective(WebPageDetail record);

    WebPageDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WebPageDetail record);

    int updateByPrimaryKey(WebPageDetail record);
}