package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


 
/**
 * 试题公用web服务
 * @author yangzhenye
 */
@Controller
@RequestMapping("/question/base")
public class QuestionController extends QuestionBaseController {

	protected String indexUrl = "redirect:/question/base/questionList";
    /**
     * 题目修改入页
     */
    public String toModifyIndex(IExamQuestionDto dto){
        return "";}
}
