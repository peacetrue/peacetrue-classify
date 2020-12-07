package com.github.peacetrue.region;

import com.github.peacetrue.core.OperatorCapableImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
public class ClassifyAdd extends OperatorCapableImpl<Long> {

    private static final long serialVersionUID = 0L;

    /** 父节点 */
    private Long parentId;
    /** 编码 */
    @NotNull
    @Size(min = 1, max = 32)
    private String code;
    /** 名称 */
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    /** 备注 */
    @Size(min = 1, max = 255)
    private String remark;

}
