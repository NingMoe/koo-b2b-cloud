package com.koolearn.cloud.queue;

import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.service.TeacherAddStudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * Created by fn on 2016/7/26.
 */
public class ConsumerMessageUserListener extends ConsumerMessageListener {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddStudentService teacherAddStudentService;

    public TeacherAddStudentService getTeacherAddStudentService() {
        return teacherAddStudentService;
    }
    public void setTeacherAddStudentService(TeacherAddStudentService teacherAddStudentService) {
        this.teacherAddStudentService = teacherAddStudentService;
    }

    @Override
    public void onMessage(Message message) {
        if ( message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                MessageContent messageContent = (MessageContent)objectMessage.getObject();//消息队列发送的对象
                if( null != messageContent){
                    Integer classesId = messageContent.getClassesId();
                    UserEntity userEntity = messageContent.getUserEntity();
                    User user = messageContent.getUser();
                    //String classNo = messageContent.getClassNo();
                    //String studentStr = messageContent.getStudentStr();
                    System.out.println( "消费队列消费用户id:" + user.getUserId() );
                   // teacherAddStudentService.addUser( user,classesId , userEntity );
                    //teacherAddStudentService.consumStudentForBlockingQueue( studentStr , classNo ,classesId , userEntity );
                }
            }
            catch (Throwable e) {
                log.error( "批量添加学生消费队列异常" + e.getMessage() , e );
            }
        }
    }
}
