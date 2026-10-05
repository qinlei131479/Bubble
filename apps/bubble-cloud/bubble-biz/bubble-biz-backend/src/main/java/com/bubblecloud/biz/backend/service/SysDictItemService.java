package com.bubblecloud.biz.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.backend.api.dto.SysDictItemSortDTO;
import com.bubblecloud.backend.api.entity.SysDictItem;
import com.bubblecloud.common.core.util.R;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
public interface SysDictItemService extends IService<SysDictItem> {

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return
	 */
	R removeDictItem(Long id);

	/**
	 * 更新字典项
	 * @param item 字典项
	 * @return
	 */
	R updateDictItem(SysDictItem item);

	/**
	 * 更新字典项排序
	 * @param sortDTO 字典项排序信息（包含字典 ID 和按目标顺序排列的字典项 ID 列表）
	 * @return 成功 / 失败
	 */
	R updateDictItemSort(SysDictItemSortDTO sortDTO);

}
