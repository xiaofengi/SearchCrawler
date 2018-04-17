package org.crawler.mysql.mapper;

import org.crawler.mysql.model.UrlRelation;

public interface UrlRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UrlRelation record);

    int insertSelective(UrlRelation record);

    UrlRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UrlRelation record);

    int updateByPrimaryKey(UrlRelation record);
}