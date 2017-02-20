package com.koolearn.cloud.queue;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * queue消息，试题对象保存队列
 */
public class ConsumerQuestionMessageListener implements MessageListener {

    private Logger logger = Logger.getLogger(ConsumerQuestionMessageListener.class);
    @Autowired
    private QuestionService questionService;
    @Autowired
    protected QuestionBaseService questionBaseService;

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    public void onMessage(Message msg) {
        if (msg instanceof ObjectMessage) {
            ObjectMessage message = (ObjectMessage) msg;
            try {
                Question question = (Question) message.getObject();//消息队列发送的对象
                try {
                    questionService.updateQuestionIndex(question);
                    //更新缓存
                    IExamQuestionDto questionDto=questionBaseService.getExamQuestionNoCache(question.getQuestionTypeId(),question.getId());
                    questionDto.getQuestionDto().setQuestion(question);
                    CacheTools.addCache(ConstantTe.REPOSITORY_QUSTION_ID + question.getId(), questionDto);
                }catch (Throwable e) {
                    logger.error("试题保存异常questionId error:"+e.getMessage());
                }
            }
            catch (Throwable e) {
                logger.error("消息队列处理失败" , e);
            }
        }
    }

    
    public QuestionService getQuestionService() {
        return questionService;
    }

    
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

}
