package com.github.peacetrue.region;

import com.github.peacetrue.core.OperatorCapableImpl;
import com.github.peacetrue.validation.constraints.multinotnull.MultiNotNull;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MultiNotNull(propertyNames = {"name", "remark", "level", "leaf", "serialNumber"})
public class ClassifyModify extends OperatorCapableImpl<Long> {

    private static final long serialVersionUID = 0L;

    /** 父节点 */
    private Long parentId;
    /** 主键 */
    @NotNull
    private Long id;
//    /** 编码 */
//    @NotNull
//    @Size(min = 1, max = 32)
//    private String code;
    /** 名称 */
    @Size(min = 1, max = 32)
    private String name;
    /** 级别 */
    private Integer level;
    /** 叶子节点 */
    private Boolean leaf;
    /** 序号 */
    private Integer serialNumber;
    /** 备注 */
    @Size(min = 1, max = 255)
    private String remark;


}
