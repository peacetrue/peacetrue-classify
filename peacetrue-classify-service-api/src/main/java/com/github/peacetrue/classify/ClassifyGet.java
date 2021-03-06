package com.github.peacetrue.classify;

import com.github.peacetrue.core.OperatorCapableImpl;
import lombok.*;


/**
 * @author xiayx
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClassifyGet extends OperatorCapableImpl<Long> {

    private static final long serialVersionUID = 0L;

    private Long id;
    private String code;
    private Long parentId;
    private Boolean leaf;

    public ClassifyGet(Long id) {
        this.id = id;
    }

    public ClassifyGet(String code) {
        this.code = code;
    }
}
