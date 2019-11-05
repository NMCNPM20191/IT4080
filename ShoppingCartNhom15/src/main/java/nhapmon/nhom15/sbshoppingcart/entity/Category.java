package nhapmon.nhom15.sbshoppingcart.entity;

import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Category")
public class Category implements Serializable {

	private static final long serialVersionUID = -1000119078147252957L;
	
	@Id
    @Column(name = "Category_id", length = 20, nullable = false)
    private int category_id;
 

	@Column(name = "Category_Name", length = 255, nullable = false)
    private String category_name;
	
	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
}
