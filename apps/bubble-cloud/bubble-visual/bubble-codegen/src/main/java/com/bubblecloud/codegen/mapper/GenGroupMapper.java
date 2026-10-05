package com.bubblecloud.codegen.mapper;

import com.bubblecloud.codegen.entity.GenGroupEntity;
import com.bubblecloud.codegen.util.vo.GroupVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 模板分组
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface GenGroupMapper extends MPJBaseMapper<GenGroupEntity> {

	GroupVO getGroupVoById(@Param("id") Long id);

}
