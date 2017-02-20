package com.koolearn.cloud.teacher.entity;
import java.io.Serializable;
import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;


@Entity
@Table(name = "location")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "type")
	private int type;
	@Column(name = "name")
	private String name;
	@Column(name = "parent_id")
	private Integer parentId;
	@Column(name = "left_id")
	private Integer leftId;
	@Column(name = "right_id")
	private Integer rightId;
	@Column(name = "tree_id")
	private Integer treeId;
	@Column(name = "level")
	private Integer level;
    @Transient
    private List< AreaCity > list;

    public List<AreaCity> getList() {
        return list;
    }

    public void setList(List<AreaCity> list) {
        this.list = list;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getLeftId() {
		return leftId;
	}

	public void setLeftId(Integer leftId) {
		this.leftId = leftId;
	}
	
	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}
	
	public Integer getTreeId() {
		return treeId;
	}

	public void setTreeId(Integer treeId) {
		this.treeId = treeId;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
