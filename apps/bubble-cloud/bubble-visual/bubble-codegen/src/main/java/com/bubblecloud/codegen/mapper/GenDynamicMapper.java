package com.bubblecloud.codegen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 动态查询
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface GenDynamicMapper {

	List<LinkedHashMap<String, Object>> dynamicQuerySql(@Param("value") String sq);

}
