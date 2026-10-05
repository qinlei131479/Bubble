package com.bubblecloud.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.codegen.entity.GenTableColumnEntity;

import java.util.List;

/**
 * 列属性
 *
 * @author qinlei
 * @date 2025/05/31
 */
public interface GenTableColumnService extends IService<GenTableColumnEntity> {

	void initFieldList(List<GenTableColumnEntity> tableFieldList);

	void syncFieldList(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList);

	void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList);

}
