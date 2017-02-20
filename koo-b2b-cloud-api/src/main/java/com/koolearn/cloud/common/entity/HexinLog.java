package com.koolearn.cloud.common.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "hexin_log")
public class HexinLog implements Serializable {
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "name")
	private String name;//接口描述
    @Column(name = "url")
    private String url;//接口地址
    @Column(name = "param")
    private String param;//提交数据
    @Column(name = "code")
    private Integer code;//状态码：code=0表示正常/code>0表示异常 code=4时，表示access_key和access_secret认证失败'
    @Column(name = "create_time")
    private Date createTime;//提交数据 result
    @Column(name = "result")
    private String result;//提交数据
    @Column(name = "optUser")
    private String optUser;//操作人

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }
}
