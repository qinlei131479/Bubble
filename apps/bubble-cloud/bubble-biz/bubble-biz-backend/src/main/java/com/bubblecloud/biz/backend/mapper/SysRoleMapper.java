package com.bubblecloud.biz.backend.mapper;

import com.bubblecloud.backend.api.entity.SysRole;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinlei
 * @since 2017-10-29
 */
@Mapper
public interface SysRoleMapper extends MPJBaseMapper<SysRole> {

	/**
	 * 通过用户ID查询角色信息
	 * @param userId 用户ID
	 * @return 角色信息列表
	 */
	List<SysRole> listRolesByUserId(@Param("userId") Long userId);

}
