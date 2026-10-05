package com.bubblecloud.biz.backend.mapper;

import com.bubblecloud.backend.api.entity.SysMenu;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author qinlei
 * @since 2017-10-29
 */
@Mapper
public interface SysMenuMapper extends MPJBaseMapper<SysMenu> {

	/**
	 * 通过角色编号查询菜单列表
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<SysMenu> listMenusByRoleId(Long roleId);

}
