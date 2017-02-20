package com.koolearn.cloud.exam.examcore.question.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "te_questiontype")
public class Questiontype implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /** 编码 */
    @Column(name = "code")
    private String code;
    /** 名称 */
    @Column(name = "name")
    private String name;
    /** 父类型 */
    @Column(name = "parent_type")
    private String parentType;
    /** 描述 */
    @Column(name = "description")
    private String description;
    /** 不用的 默认0 表示使用 */
    @Column(name = "disable")
    private Integer disable;
    /** 单独显示的,不依赖其他题型,模式是单独显示1 */
    @Column(name = "alone")
    private Integer alone;
    public  Questiontype()
    {

    }
    public  Questiontype(int id,String name)
    {
        this.id = id;
        this.name = name;
    }


    public  Questiontype(int id)
    {
        this.setId(id);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public Integer getAlone() {
        return alone;
    }

    public void setAlone(Integer alone) {
        this.alone = alone;
    }
}
