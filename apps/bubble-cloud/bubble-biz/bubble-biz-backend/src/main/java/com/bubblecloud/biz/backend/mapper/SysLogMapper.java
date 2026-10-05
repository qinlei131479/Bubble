package com.bubblecloud.biz.backend.mapper;

import com.bubblecloud.backend.api.entity.SysLog;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author qinlei
 */
@Mapper
public interface SysLogMapper extends MPJBaseMapper<SysLog> {

	/**
	 * 按日期和日志类型聚合日志数量
	 * @param startTime 开始时间
	 * @return 聚合结果
	 */
	List<Map<String, Object>> selectLogSum(@Param("startTime") LocalDateTime startTime);

}
