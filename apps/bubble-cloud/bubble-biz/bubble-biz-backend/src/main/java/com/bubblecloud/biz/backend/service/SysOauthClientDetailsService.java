package com.bubblecloud.biz.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.backend.api.dto.SysOauthClientDetailsDTO;
import com.bubblecloud.backend.api.entity.SysOauthClientDetails;
import com.bubblecloud.common.core.util.R;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

	/**
	 * 更新 OAuth 客户端配置，并失效客户端详情缓存。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含主键和客户端ID
	 * @return 更新是否成功
	 */
	Boolean updateClientById(SysOauthClientDetailsDTO clientDetailsDTO);

	/**
	 * 新增 OAuth 客户端配置，并失效对应客户端详情缓存。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含客户端ID、密钥和授权范围
	 * @return 新增是否成功
	 */
	Boolean saveClient(SysOauthClientDetailsDTO clientDetailsDTO);

	/**
	 * 分页查询 OAuth 客户端配置。
	 * @param page 分页参数
	 * @param query 客户端查询条件，可为空字段表示不参与过滤
	 * @return 客户端配置分页数据
	 */
	Page queryPage(Page page, SysOauthClientDetails query);

	/**
	 * 清空客户端详情缓存，下次认证访问时重新查库。
	 * @return 操作结果
	 */
	R syncClientCache();

	/**
	 * 按主键批量删除客户端并失效客户端缓存。
	 * @param ids 主键ID 列表
	 * @return 是否成功
	 */
	Boolean removeClientByIds(Long[] ids);

}
