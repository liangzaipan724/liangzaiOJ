package com.liangzai.liangzaiOJ.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息消息枚举
 *
 * @author <a href="https://github.com/liliangzai">程序员鱼皮</a>
 * @from <a href="https://liangzai.icu">编程导航知识星球</a>
 */
public enum QuestionSubmitJudgeMessageEnum {
    ACCEPT("成功","Accepted"),
    WRONG_ANSWER("答案错误","Wrong Answer"),
    COMPILE_ERROR("Compile Error","编译错误"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded","内存溢出"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded","超时"),
    PRESENTATION("Presentation","展示错误"),
     WAITTING("Waitting","等待中"),
     OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded","输出溢出"),
     DANGEROUS_OPERATION("Dangerous Operation","危险操作"),
     RUNTIME_ERROR("Runtime Error","运行错误"),
    SYSTEM_ERROR("System Error", "系统错误");

    private final String text;

    private final String value;

    QuestionSubmitJudgeMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitJudgeMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitJudgeMessageEnum anEnum : QuestionSubmitJudgeMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
