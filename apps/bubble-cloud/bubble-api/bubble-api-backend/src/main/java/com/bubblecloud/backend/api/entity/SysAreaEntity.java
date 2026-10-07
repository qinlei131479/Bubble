package com.bubblecloud.backend.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 行政区划表
 *
 * @author lbw
 * @date 2024-02-16 21:59:02
 */
@Data
@FieldNameConstants
@TableName("sys_area")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "行政区划表")
public class SysAreaEntity extends Model<SysAreaEntity> {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 父ID
	 */
	@Schema(description = "父ID")
	private Long pid;

	/**
	 * 地区名称
	 */
	@Schema(description = "地区名称")
	private String name;

	/**
	 * 简称
	 */
	@Schema(description = "简称")
	private String shortName;

	/**
	 * 层级深度，0省，1市，2区，3镇
	 */
	@Schema(description = "层级深度，0省，1市，2区，3镇")
	private Integer deep;

	/**
	 * 地区字母
	 */
	@Schema(description = "地区字母")
	private String letter;

	/**
	 * 完整拼音
	 */
	@Schema(description = "完整拼音")
	private String pinyin;

	/**
	 * 高德地区code
	 */
	@Schema(description = "地区code")
	private Long adcode;

	/**
	 * 经纬度
	 */
	@Schema(description = "经纬度")
	private String location;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Long areaSort;

	/**
	 * 0:未生效，1:生效
	 */
	@Schema(description = "0:未生效，1:生效")
	private String areaStatus;

	/**
	 * 0:国家,1:省,2:城市,3:区县
	 */
	@Schema(description = "0国家，1省，2城市，3区县，4街道")
	private String areaType;

	/**
	 * 0:非热门，1:热门
	 */
	@Schema(description = "0:非热门，1:热门")
	private String hot;

	/**
	 * 城市编码
	 */
	@Schema(description = "城市编码")
	private String cityCode;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记")
	private String delFlag;

}
