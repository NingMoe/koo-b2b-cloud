package com.koolearn.cloud.queue;

import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
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
public class ConsumerPaperMessageListener implements MessageListener {

    private Logger logger = Logger.getLogger(ConsumerPaperMessageListener.class);
    @Autowired
    private QuestionService questionService;


    public void onMessage(Message msg) {
        if (msg instanceof ObjectMessage) {
            ObjectMessage message = (ObjectMessage) msg;
            try {
                TestPaper testPaper = (TestPaper) message.getObject();//消息队列发送的对象
                try {
                    questionService.updatePaperIndex(testPaper);
                }catch (Throwable e) {
                    logger.error("试卷保存异常error:"+e.getMessage());
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
