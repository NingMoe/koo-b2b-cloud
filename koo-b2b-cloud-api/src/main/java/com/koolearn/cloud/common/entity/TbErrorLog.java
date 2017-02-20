package com.koolearn.cloud.common.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fn on 2016/11/30.
 */
@Entity
@Table(name = "tb_error_log")
public class TbErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /** 日志类型（1: 小学说课批量增加教师 ，2：学校端批量增加教师 ） */
    @Column(name = "type")
    private Integer type;
    /** 接口名称*/
    @Column(name = "interface_name")
    private String interfaceName;
    /** 日志描述记录 */
    @Column(name = "log_info")
    private String logInfo;
    /** 创建日期 */
    @Column(name = "create_time")
    private Date createTime;
    /** 备注 */
    @Column(name = "remark")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
