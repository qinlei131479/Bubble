package com.bubblecloud.biz.backend.mapper;

import com.bubblecloud.backend.api.entity.SysPost;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author qinlei
 * @date 2025/06/27
 */
@Mapper
public interface SysPostMapper extends MPJBaseMapper<SysPost> {

	/**
	 * 通过用户ID查询岗位信息
	 * @param userId 用户ID
	 * @return 岗位信息列表
	 */
	List<SysPost> listPostsByUserId(@Param("userId") Long userId);

}
