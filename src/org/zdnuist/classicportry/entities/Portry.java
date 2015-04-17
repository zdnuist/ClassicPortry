package org.zdnuist.classicportry.entities;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "portries")
public class Portry extends EntityBase {

	@Column(column = "content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		 return "Portry{" +
	                "id=" + getId() +
	                ", content='" + content + '\'' +
	                '}';
	}

}
