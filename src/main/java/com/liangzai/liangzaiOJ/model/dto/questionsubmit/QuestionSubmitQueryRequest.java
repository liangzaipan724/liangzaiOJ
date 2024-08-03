package com.liangzai.liangzaiOJ.model.dto.questionsubmit;

import com.liangzai.liangzaiOJ.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liliangzai">程序员鱼皮</a>
 * @from <a href="https://liangzai.icu">编程导航知识星球</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {


    /**
     * 编程语言
     */
    private String language;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}