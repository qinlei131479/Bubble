package com.bubblecloud.biz.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.backend.api.dto.UserDTO;
import com.bubblecloud.backend.api.entity.SysUser;
import com.bubblecloud.backend.api.vo.UserVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author qinlei
 * @since 2017-10-29
 */
@Mapper
public interface SysUserMapper extends MPJBaseMapper<SysUser> {

	/**
	 * 通过用户DTO查询用户信息（包含角色信息）
	 * @param userDTO 用户查询DTO
	 * @return 包含角色信息的用户VO
	 */
	UserVO getUserVo(@Param("query") UserDTO userDTO);

	/**
	 * 分页查询用户信息（含角色）
	 * @param page 分页对象
	 * @param userDTO 用户查询参数
	 * @return 分页用户信息列表
	 */
	IPage<UserVO> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息VO对象
	 */
	UserVO getUserVoById(Long id);

	/**
	 * 查询用户列表
	 * @param userDTO 查询条件
	 * @param ids 用户ID数组
	 * @return 用户VO列表
	 */
	List<UserVO> getUserVoList(@Param("query") UserDTO userDTO, @Param("ids") Long[] ids);

}
