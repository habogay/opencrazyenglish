package net.cec.entity;


public class Member {

	private String id;
	
	private String avatar;

	private String name;

	public Member(String id) {
		this.setId(id);

	}
	public Member() {
		

	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
