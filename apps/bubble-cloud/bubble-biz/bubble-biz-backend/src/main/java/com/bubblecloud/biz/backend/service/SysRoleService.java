package com.bubblecloud.biz.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.backend.api.entity.SysRole;
import com.bubblecloud.backend.api.vo.RoleExcelVO;
import com.bubblecloud.backend.api.vo.RoleMenuVO;
import com.bubblecloud.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * <p>
 * 角色服务类
 * </p>
 *
 * @author qinlei
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通过用户ID查询其拥有的角色信息
	 * @param userId 用户ID
	 * @return 该用户绑定的角色列表，无角色时返回空列表
	 */
	List<SysRole> findRolesByUserId(Long userId);

	/**
	 * 根据角色ID列表查询角色，结果按缓存key缓存
	 * @param roleIdList 角色ID列表
	 * @param key 缓存key
	 * @return 匹配的角色列表，无匹配时返回空列表
	 */
	List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key);

	/**
	 * 通过角色ID批量删除角色，并清理关联的角色菜单
	 * @param ids 角色ID数组
	 * @return 删除成功返回 true
	 */
	Boolean removeRoleByIds(Long[] ids);

	/**
	 * 更新角色的菜单授权
	 * @param roleVo 角色及其菜单ID列表
	 * @return 更新成功返回 true
	 */
	Boolean updateRoleMenus(RoleMenuVO roleVo);

	/**
	 * 导入角色，按角色名称或角色编码去重，重复数据不入库并收集错误信息
	 * @param excelVOList 待导入的角色列表
	 * @param bindingResult 通用校验结果，其 target 持有错误信息列表
	 * @return 全部导入成功返回 ok；存在校验失败时返回携带错误信息列表的 failed
	 */
	R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 查询角色并转换为导出对象
	 * @param sysRole 查询条件
	 * @param ids 指定导出的角色ID数组，为空时按查询条件导出全部
	 * @return 角色导出对象列表
	 */
	List<RoleExcelVO> listRole(SysRole sysRole, Long[] ids);

}
