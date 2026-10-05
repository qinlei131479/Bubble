package com.bubblecloud.codegen.mapper;

import com.bubblecloud.codegen.entity.GenTemplateEntity;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模板
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface GenTemplateMapper extends MPJBaseMapper<GenTemplateEntity> {

	/**
	 * 根据groupId查询 模板
	 * @param groupId
	 * @return
	 */
	List<GenTemplateEntity> listTemplateById(Long groupId);

}
