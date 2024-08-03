package com.liangzai.liangzaiOJ.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liangzai.liangzaiOJ.annotation.AuthCheck;
import com.liangzai.liangzaiOJ.common.BaseResponse;
import com.liangzai.liangzaiOJ.common.ErrorCode;
import com.liangzai.liangzaiOJ.common.ResultUtils;
import com.liangzai.liangzaiOJ.constant.UserConstant;
import com.liangzai.liangzaiOJ.exception.BusinessException;
import com.liangzai.liangzaiOJ.model.dto.question.QuestionQueryRequest;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangzai.liangzaiOJ.model.entity.Question;
import com.liangzai.liangzaiOJ.model.entity.QuestionSubmit;
import com.liangzai.liangzaiOJ.model.entity.User;
import com.liangzai.liangzaiOJ.model.vo.QuestionSubmitVO;
import com.liangzai.liangzaiOJ.service.QuestionSubmitService;
import com.liangzai.liangzaiOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liliangzai">程序员鱼皮</a>
 * @from <a href="https://liangzai.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                         HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
//        long postId = questionSubmitAddRequest.getQuestionId();
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest,loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案，提交代码等公开信息）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest
            , HttpServletRequest request){
        long current=questionSubmitQueryRequest.getCurrent();
        long size=questionSubmitQueryRequest.getPageSize();
        //从数据库中查询原始的题目提交分页列表
        Page<QuestionSubmit> questionSubmitPage=questionSubmitService.page(new Page<>(current,size),questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        //返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage,request));
    }
}
