package com.bubblecloud.biz.backend.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.backend.api.entity.SysDept;
import com.bubblecloud.backend.api.vo.DeptExcelVO;
import com.bubblecloud.backend.api.vo.OrgTreeVO;
import com.bubblecloud.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查询部门树菜单
	 * @param deptName 部门名称
	 * @return 树
	 */
	List<Tree<Long>> selectTree(String deptName, Long parentId);

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	Boolean removeDeptById(Long id);

	/**
	 * 获取部门Excel视图对象列表
	 * @return 部门Excel视图对象列表
	 */
	List<DeptExcelVO> listExcelVo();

	/**
	 * 导入部门数据
	 * @param excelVOList 部门Excel数据列表
	 * @param bindingResult 数据绑定结果
	 * @return 导入结果
	 */
	R importDept(List<DeptExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 获取部门的所有后代部门列表
	 * @param deptId 部门ID
	 * @return 后代部门列表
	 */
	List<SysDept> listDescendant(Long deptId);

	/**
	 * 查询全部部门包含用户
	 * @param parentDeptId 父部门ID
	 * @param type 查询类型
	 */
	Map<String, Object> listOrgTree(Long parentDeptId, String type);

	/**
	 * 根据用户名模糊搜索用户
	 * @param username 用户名
	 * @return List user
	 */
	List<OrgTreeVO> getOrgTreeUser(String username);

}
