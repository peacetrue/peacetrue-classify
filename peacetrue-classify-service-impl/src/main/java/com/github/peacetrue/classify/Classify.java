package com.github.peacetrue.classify;

import com.github.peacetrue.core.IdCapable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体类
 *
 * @author xiayx
 */
@Getter
@Setter
@ToString
public class Classify implements Serializable, IdCapable<Long> {

    private static final long serialVersionUID = 0L;

    /** 主键 */
    @Id
    private Long id;
    /** 编码 */
    private String code;
    /** 名称 */
    private String name;
    /** 备注 */
    private String remark;
    /** 类型 */
    private Long typeId;
    /** 类型编码 */
    private String typeCode;
    /** 父节点 */
    private Long parentId;
    /** 层级 */
    private Integer level;
    /** 叶子节点 */
    private Boolean leaf;
    /** 序号 */
    private Integer serialNumber;
    /** 创建者主键 */
    private Long creatorId;
    /** 创建时间 */
    private LocalDateTime createdTime;
    /** 修改者主键 */
    private Long modifierId;
    /** 最近修改时间 */
    private LocalDateTime modifiedTime;

}
