package com.github.peacetrue.classify;

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

    /** 主键 */
    @NotNull
    private Long id;
    /** 名称 */
    @Size(min = 1, max = 32)
    private String name;
    /** 备注 */
    @Size(min = 1, max = 255)
    private String remark;


}
