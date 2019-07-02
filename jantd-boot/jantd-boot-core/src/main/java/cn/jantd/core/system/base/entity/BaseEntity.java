package cn.jantd.core.system.base.entity;

import cn.jantd.core.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description Entity基类,和数据库的表的字段是对应的
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseEntity {

	/** ID */
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "ID")
	private java.lang.String id;
	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/** 创建时间 */
	@ApiModelProperty(value = "创建时间")
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/** 更新人 */
	@ApiModelProperty(value = "更新人")
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/** 更新时间 */
	@ApiModelProperty(value = "更新时间")
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
