package org.crawler.mysql.mapper;

import org.apache.ibatis.annotations.Param;
import org.crawler.mysql.mapper.base.BaseMapper;
import org.crawler.mysql.model.UrlRelation;

import java.util.List;

public interface UrlRelationMapper extends BaseMapper<UrlRelation>{

    /**
     * 批量插入url关系
     * @param urlRelationLs
     */
    void batchInsert(@Param(value = "urlRelationLs") List<UrlRelation> urlRelationLs);
}