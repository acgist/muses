package com.acgist.user.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.acgist.data.pojo.entity.StateEntity;

@Entity
@Table(name = "t_role")
public class RoleEntity extends StateEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_PATHS = "paths";
	
	/**
	 * 权限
	 */
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(
	name = "t_role_path",
		joinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_path_role_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "path_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_path_path_id")
		)
	)
	private List<PathEntity> paths;

	public List<PathEntity> getPaths() {
		return paths;
	}

	public void setPaths(List<PathEntity> paths) {
		this.paths = paths;
	}
	
}
