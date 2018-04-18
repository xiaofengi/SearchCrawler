package org.crawler.mysql.mapper.base;

import org.crawler.mysql.model.UrlRelation;

public interface BaseMapper<Model>{

    int deleteByPrimaryKey(Long id);

    int insert(Model record);

    int insertSelective(Model record);

    Model selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Model record);

    int updateByPrimaryKey(Model record);

}
