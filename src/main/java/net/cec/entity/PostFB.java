package net.cec.entity;

import java.util.List;

public class PostFB {
	
	private long id;
	
	private String url;
	
	private String posterID;
	
	
	private List<String> likes;
	
	private List<String> comments;
	
	private String content;
	
	
	public PostFB(String posterID){
		this.id=System.currentTimeMillis();
		this.posterID=posterID;
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPosterID() {
		return posterID;
	}
	public void setPosterID(String posterID) {
		this.posterID = posterID;
	}
	
	public List<String> getLikes() {
		return likes;
	}
	public void setLikes(List<String> likes) {
		this.likes = likes;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
