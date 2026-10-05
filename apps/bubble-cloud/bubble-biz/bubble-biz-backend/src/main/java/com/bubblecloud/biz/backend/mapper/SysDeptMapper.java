package com.bubblecloud.biz.backend.mapper;

import com.bubblecloud.backend.api.entity.SysDept;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 Mapper 接口
 *
 * @author qinlei
 * @since 2018-01-20
 */
@Mapper
public interface SysDeptMapper extends MPJBaseMapper<SysDept> {

	/**
	 * 根据用户ID查询部门列表
	 * @param userId 用户ID
	 * @return 部门列表
	 */
	List<SysDept> listDeptsByUserId(@Param("userId") Long userId);

}
