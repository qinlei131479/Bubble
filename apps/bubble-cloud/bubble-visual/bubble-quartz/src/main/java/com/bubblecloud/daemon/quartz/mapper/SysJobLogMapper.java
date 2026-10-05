package com.bubblecloud.daemon.quartz.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.bubblecloud.daemon.quartz.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务执行日志表
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface SysJobLogMapper extends MPJBaseMapper<SysJobLog> {

}
