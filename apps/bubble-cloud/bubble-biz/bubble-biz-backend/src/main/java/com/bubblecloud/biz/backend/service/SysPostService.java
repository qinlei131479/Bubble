package com.bubblecloud.biz.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.backend.api.entity.SysPost;
import com.bubblecloud.backend.api.vo.PostExcelVO;
import com.bubblecloud.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-03-26 12:50:43
 */
public interface SysPostService extends IService<SysPost> {

	/**
	 * 按传入 ID 顺序调整岗位，保留未选中记录的位置。
	 * @param ids 非空且无重复的有效岗位 ID 列表
	 */
	void sort(List<Long> ids);

	/**
	 * 导出excel 表格
	 * @return
	 */
	List<PostExcelVO> listPost(SysPost post, Long[] ids);

	/**
	 * 导入岗位
	 * @param excelVOList 岗位列表
	 * @param bindingResult 通用校验结果，其 target 持有错误信息列表
	 * @return 全部导入成功返回 ok；存在校验失败时返回携带错误信息列表的 failed
	 */
	R importPost(List<PostExcelVO> excelVOList, BindingResult bindingResult);

}
