package com.github.peacetrue.classify;

import com.github.peacetrue.core.OperatorCapableImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
public class ClassifyAdd extends OperatorCapableImpl<Long> {

    private static final long serialVersionUID = 0L;

    /** 父节点 */
    @NotNull
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
    /** 子节点 */
    private List<ClassifyAdd> children;

    public ClassifyAdd() {
    }

    public ClassifyAdd(Long parentId, String code, String name) {
        this(parentId, code, name, (String) null);
    }

    public ClassifyAdd(Long parentId, String code, String name, String remark) {
        this(parentId, code, name, remark, (List<ClassifyAdd>) null);
    }

    public ClassifyAdd(String code, String name, ClassifyAdd... children) {
        this(null, code, name, null, children);
    }


    public ClassifyAdd(String code, String name, String remark, ClassifyAdd... children) {
        this(null, code, name, remark, children);
    }

    public ClassifyAdd(Long parentId, String code, String name, ClassifyAdd... children) {
        this(parentId, code, name, null, children);
    }

    public ClassifyAdd(Long parentId, String code, String name, String remark, ClassifyAdd... children) {
        this(parentId, code, name, remark, children != null && children.length > 0 ? Arrays.asList(children) : null);
    }

    public ClassifyAdd(Long parentId, String code, String name, String remark, List<ClassifyAdd> children) {
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.remark = remark;
        this.children = children;
    }


}
