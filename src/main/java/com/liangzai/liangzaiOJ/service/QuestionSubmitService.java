package com.liangzai.liangzaiOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liangzai.liangzaiOJ.model.dto.question.QuestionQueryRequest;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangzai.liangzaiOJ.model.entity.Question;
import com.liangzai.liangzaiOJ.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzai.liangzaiOJ.model.entity.User;
import com.liangzai.liangzaiOJ.model.vo.QuestionSubmitVO;
import com.liangzai.liangzaiOJ.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author panhao
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-07-30 23:45:21
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestion1SubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request);
}
