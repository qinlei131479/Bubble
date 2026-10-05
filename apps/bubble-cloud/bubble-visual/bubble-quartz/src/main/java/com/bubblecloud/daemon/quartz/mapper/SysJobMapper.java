package com.bubblecloud.daemon.quartz.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.bubblecloud.daemon.quartz.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务调度表
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Mapper
public interface SysJobMapper extends MPJBaseMapper<SysJob> {

}
