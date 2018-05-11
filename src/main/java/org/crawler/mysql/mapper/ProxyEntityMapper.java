package org.crawler.mysql.mapper;

import org.apache.ibatis.annotations.Param;
import org.crawler.mysql.mapper.base.BaseMapper;
import org.crawler.mysql.model.ProxyEntity;

import java.util.List;

public interface ProxyEntityMapper extends BaseMapper<ProxyEntity>{

    void batchInsert(@Param(value = "proxyEntityList") List<ProxyEntity> proxyEntityList);
}