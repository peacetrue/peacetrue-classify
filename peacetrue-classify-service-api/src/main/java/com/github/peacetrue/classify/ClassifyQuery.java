package com.github.peacetrue.classify;

import com.github.peacetrue.core.OperatorCapableImpl;
import com.github.peacetrue.core.Range;
import lombok.*;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClassifyQuery extends OperatorCapableImpl<Long> {

    public static final ClassifyQuery DEFAULT = new ClassifyQuery();

    private static final long serialVersionUID = 0L;

    /** 主键 */
    private Long[] id;
    /** 编码 */
    private String code;
    /** 名称 */
    private String name;
    /** 类型 */
    private Long typeId;
    /** 类型编码 */
    private String typeCode;
    /** 父节点 */
    private Long parentId;
    /** 层级 */
    private Integer level;
    /** 创建者主键 */
    private Long creatorId;
    /** 创建时间 */
    private Range.LocalDateTime createdTime;
    /** 修改者主键 */
    private Long modifierId;
    /** 最近修改时间 */
    private Range.LocalDateTime modifiedTime;

    public ClassifyQuery(Long[] id) {
        this.id = id;
    }

    public ClassifyQuery(String code) {
        this.code = code;
    }

    public static ClassifyQuery fromParentId(Long parentId) {
        ClassifyQuery classifyQuery = new ClassifyQuery();
        classifyQuery.setParentId(parentId);
        return classifyQuery;
    }


}
