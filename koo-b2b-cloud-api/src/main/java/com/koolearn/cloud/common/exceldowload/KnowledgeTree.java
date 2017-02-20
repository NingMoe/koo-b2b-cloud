package com.koolearn.cloud.common.exceldowload;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 *    （作用在get方法上）
 *    @XmlRootElement：根节点
      @XmlAttribute：该属性作为xml的attribute
      @XmlElement：该属性作为xml的element，且可以增加属性(name="NewElementName")，那么生成的xml串的elment的标签是NewElementName
      @XmlTransient :阻止把该属性映射到xml
 */
@XmlRootElement(name="knowledgeRoot")
public class KnowledgeTree implements Serializable{
	
	private static final long serialVersionUID = -1520061513011417451L;
	
	private Integer id;
	
	private String name;
	
	private Integer pid;
	
	private List<KnowledgeTree> children;
    @XmlAttribute
	public Integer getId() {
		return id;
	}
    @XmlAttribute
    public Integer getPid() {
		return pid;
	}
    @XmlElement
    public String getName() {
        return name;
    }
    @XmlElement(name = "knowledge")
	public List<KnowledgeTree> getChildren() {
		return children;
	}
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setChildren(List<KnowledgeTree> children) {
        this.children = children;
    }
}
