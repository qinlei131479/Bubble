package com.bubblecloud.codegen.mapper;

import com.bubblecloud.codegen.entity.GenFieldType;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 列属性
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface GenFieldTypeMapper extends MPJBaseMapper<GenFieldType> {

	/**
	 * 根据tableId，获取包列表
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return 返回包列表
	 */
	Set<String> getPackageByTableId(@Param("dsName") String dsName, @Param("tableName") String tableName);

}
