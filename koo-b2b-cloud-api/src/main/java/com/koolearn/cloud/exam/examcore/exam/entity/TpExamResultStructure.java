package com.koolearn.cloud.exam.examcore.exam.entity;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "te_exam_result_structure")
public class TpExamResultStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int TYPE_BIG_QUESTION = 1;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    @Column(name = "result_id")
    private Integer resultId;
    @Column(name = "descript")
    private String descript;
    /** 父考试结果结构id ，自关联 */
    @Column(name = "parent")
    private Integer parent;
    /** 排序 */
    @Column(name = "odr")
    private Integer odr;
    @Column(name = "name")
    private String name;
    @Column(name = "point")
    private Double point;
    @Column(name = "timeout")
    private Integer timeout;
    @Column(name = "type")
    private Integer type;
    private List<TpExamResultStructure> children; // 子结构

    private List<TpExamResultDetail> questions; // 结构下对应的结果明细
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getOdr() {
        return odr;
    }

    public void setOdr(Integer odr) {
        this.odr = odr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPoint() {
        if (null != this.point)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            return Double.valueOf(nf.format(this.point));
        }
        else
        {
            return this.point;
        }
    }

    public void setPoint(Double point) {
        if (null != point)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            this.point = Double.valueOf(nf.format(point));
        }
        else
        {
            this.point = point;
        }
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<TpExamResultStructure> getChildren() {
        return children;
    }

    public void setChildren(List<TpExamResultStructure> children) {
        this.children = children;
    }

    public List<TpExamResultDetail> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TpExamResultDetail> questions) {
        this.questions = questions;
    }
}
