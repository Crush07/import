package entity;

import annotate.Note;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hysea
 */
@Data
public class Course implements Serializable {

	@Note(value = "id")
	public String id;

	@Note(value = "科目id")
	public String subject;

	@Note(value = "老师id")
	public String userId;

	@Note(value = "班级id")
	public String classId;

	@Note(value = "上课地点")
	public String address;

	@Note(value = "周")
	public Integer week;

	@Note(value = "日")
	public Integer dayOfWeek;

	@Note(value = "节次")
	public Integer sort;

	@Note(value = "是否是长期")
	public Integer isLongTerm;

	@Note(value = "学期")
	public Integer stage;

	@Note(value = "学年")
	public Integer year;

	@Note(value = "创建时间")
	public Date createTime;

	@Note(value = "修改时间")
	public Date updateTime;


}