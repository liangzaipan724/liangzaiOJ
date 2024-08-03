package com.liangzai.liangzaiOJ.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzai.liangzaiOJ.common.ErrorCode;
import com.liangzai.liangzaiOJ.constant.CommonConstant;
import com.liangzai.liangzaiOJ.exception.BusinessException;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liangzai.liangzaiOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liangzai.liangzaiOJ.model.entity.Question;
import com.liangzai.liangzaiOJ.model.entity.QuestionSubmit;
import com.liangzai.liangzaiOJ.model.entity.User;
import com.liangzai.liangzaiOJ.model.enums.QuestionSubmitLanguageEnum;
import com.liangzai.liangzaiOJ.model.enums.QuestionSubmitStatusEnum;
import com.liangzai.liangzaiOJ.model.vo.QuestionSubmitVO;
import com.liangzai.liangzaiOJ.model.vo.QuestionVO;
import com.liangzai.liangzaiOJ.model.vo.UserVO;
import com.liangzai.liangzaiOJ.service.QuestionService;
import com.liangzai.liangzaiOJ.service.QuestionSubmitService;
import com.liangzai.liangzaiOJ.mapper.QuestionSubmitMapper;
import com.liangzai.liangzaiOJ.service.UserService;
import com.liangzai.liangzaiOJ.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import java.util.stream.Collectors;

/**
* @author panhao
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-07-30 23:45:21
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
//    @Resource
//    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;
    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(languageEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        // 判断实体是否存在，根据类别获取实体
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();

//         锁必须要包裹住事务方法
//        QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
//        synchronized (String.valueOf(userId).intern()) {
//            return questionSubmitService.doQuestionSubmitInner(userId, questionId);
//    }
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit=new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"输出插入失败");
        }
        return  questionSubmit.getId();
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的QueryWrapper类）
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjectUtils.isNotEmpty(questionId),"questionId",questionId);
        queryWrapper.like(QuestionSubmitStatusEnum.getEnumByValue(status)!=null,"status",status);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long questionSubmitId = questionSubmit.getId();
        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionSubmitVO.setUserVO(userVO);
        //关联查询题目信息
        Long questionId = questionSubmit.getQuestionId();
        Question question=null;
        if(questionId!=null&&questionId>0){
            question=questionService.getById(questionId);
        }
        QuestionVO questionVO = questionService.getQuestionVO(question,request);
        questionSubmitVO.setQuestionVO(questionVO);
        //脱敏；仅本人和管理员能看到自己（提交userId和登录用户Id相同）的提交代码
        User loginUser = userService.getLoginUser(request);
        Long loginUserid = loginUser.getId();
        if(!userService.isAdmin(loginUser)&&loginUserid!=questionSubmit.getUserId()){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, request))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




