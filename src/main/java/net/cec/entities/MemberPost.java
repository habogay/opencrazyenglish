package net.cec.entities;





public class MemberPost {
// String id, String attachments, String type, String content, Long createDate, String featuredImage, Long lastupdate, String permalink, String picture, String poster, String posterId 
	
	private String id;
	
	
	private String attachments = null;
	
	
	private String type;
	
	
	private String content;
	
	
	private Long createdDate;

	
	private Long lastUpdate;
	
	
	private String permalink;
	
	
	private String picture;

	
	private String posterId;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosterId() {
		return posterId;
	}

	public void setPosterId(String posterId) {
		this.posterId = posterId;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
}
