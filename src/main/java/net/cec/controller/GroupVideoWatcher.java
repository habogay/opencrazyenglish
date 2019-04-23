package net.cec.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.fcs.Utils;
import net.cec.Secret;
import net.cec.entities.MemberPost;
import net.cec.entities.MemberVideo;
import net.cec.entity.Member;
import net.cec.repository.FireStoreRepository;
import net.cec.repository.GoogleDriveRepository;

public class GroupVideoWatcher extends Thread {

	static Logger log = Logger.getLogger(GroupVideoWatcher.class.getName());

	public static void main(String[] args) {

		String urlFB = "http://facebook.com";
		String email = Secret.email;
		String password = Secret.password;
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		
		ChromeOptions options = new ChromeOptions();
		
		options.setExperimentalOption("prefs", prefs);
		WebDriver driver = new ChromeDriver(options);
		System.out.println("urlFb: "+urlFB);
		driver.get(urlFB);

		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("pass")).sendKeys(password + Keys.ENTER);
		
		while (1 == 1) {
			try {
				crawlVideo(driver);
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(UnhandledAlertException uae)
				{
					driver.switchTo().alert().accept();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				

			}
		}
		
		
	}
	
	
	/**
	 * Find new the videos and insert into the database
	 * @param webDriver - selenium web driver
	 * */
	
	public static void findNewVideo(WebDriver driver) { 
		driver.get("https://www.facebook.com/groups/cec.edu.vn/videos/");

		List<WebElement> videosDiv = driver.findElements(By.className("itemsPlutoniumRedesign"));
		ArrayList<MemberVideo> memberVideoList = new ArrayList<MemberVideo>();
		for (WebElement webElement : videosDiv) {

			try {

				String video_id = webElement.findElement(By.tagName("a")).getAttribute("href");
				if (video_id.startsWith("https://www.facebook.com/")) {

					System.out.println("Found " + video_id);
					String videoUrl =  video_id;
					video_id = video_id.split("/")[5];
// 					check post exist or not
					boolean exist = FireStoreRepository.getFireStore().collection("Video").document(video_id).get().get().exists();
					if (exist) {
						System.out.println("But it exist !!! then break  ");
						break;
					}
					else
					{
						MemberVideo mv = new MemberVideo();
						mv.setId(video_id);
						mv.setStatus("new");
						mv.setUrl(videoUrl);
						memberVideoList.add(mv);
//						insert new the videos to db
						FireStoreRepository.getFireStore().collection("Video").document(video_id).set(mv);
					}
					
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		
	}
	
	
	
	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
	
	public static void commentVideo(WebDriver driver, MemberVideo mv, MemberPost mp) {

		String post_id = mp.getId().replaceAll("1784461175160264_", "");
		
		String comment_content = String.format(GoogleDriveRepository.getRandomComment(), mv.getPosterName(), post_id);
		
		String video_id = mv.getPermalink();
		video_id = video_id.replaceAll("www", "m");
		System.out.println("video_id:" + video_id);
		driver.get(video_id);
		driver.findElement(By.cssSelector("a._15ko")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("a._15kq")).click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement currentElement = driver.switchTo().activeElement();

		currentElement.sendKeys(comment_content);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentElement.submit();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Find lesson string ... "+mp.getContent().toLowerCase().contains("les"));
		String to = mp.getPosterId();
		if (mp.getContent().toLowerCase().contains("les")) {
			
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(mp.getContent().toLowerCase());
			if (m.find()) {
				System.out.println(m.group());
				int lesson = Integer.parseInt(m.group());
				System.out.println("Find lesson number :  "+lesson);
				String urlAltp="";
				String contentAltp = "";
				
				if(lesson<=23)
				{
					lesson++;
					String validate = MD5(lesson + mp.getPosterId());
					contentAltp = "Chào "+mv.getPosterName()+" đây là link bài tiếp theo : https://cec.net.vn/lesson/" + lesson + "?v=" + validate+"&me="+mp.getPosterId();	
				}
				
				if(lesson==24)
				{
					contentAltp = String.format(GoogleDriveRepository.getRandomMgsAltp(), mv.getPosterName());
//					content = "Chúc mừng "+mv.getPosterName()+" đã hoàn thành 24 lessons trong Ai Là Triệu Phú. Bạn là thiên tài của mình, rất ít người làm được như bạn.Chờ bạn trong những hành trình tiếp theo";
					System.out.println(contentAltp);
				}
				
				if(lesson<=24)
				{
					try {
						//urlAltp = "https://us-central1-opencec.cloudfunctions.net/sendMgs?message="+contentAltp+"&to="+to;
						messengerNextLesson(driver,post_id,contentAltp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("altp content: "+urlAltp);
				
			}

		}

		Pattern p = Pattern.compile("(\\d+)(/|\\.)(\\d+)");
		Matcher m = p.matcher(mp.getContent().toLowerCase());
		if (m.find()) {

			String spliter[] = m.group().split("/|\\.");
			int current = Integer.parseInt(spliter[0]);
			int total = Integer.parseInt(spliter[1]);
//			"Chúc mừng %s đã hoàn thành video số %n trong hành trình %t ngày nói tiếng anh liên tục. Tiếp tục cố gắng nhé, mình sẽ theo dõi hành trình của bạn.";
			String contentXday = "Chúc mừng "+mv.getPosterName()+" đã hoàn thành video số " + current + " trong hành trình " + total
					+ " ngày nói tiếng anh liên tục. Tiếp tục cố gắng nhé, mình sẽ theo dõi hành trình của bạn.";
//			"Chúc mừng %s đã hoàn thành hành trình %t ngày nói tiếng anh liên tục. Bạn là thiên tài của mình, rất ít người làm được như bạn.Chờ bạn trong những hành trình tiếp theo";
			if(current==total)
			{
//				msg = "Chúc mừng "+mv.getPosterName()+" đã hoàn thành hành trình " + total
//						+ " ngày nói tiếng anh liên tục. Bạn là thiên tài của mình, rất ít người làm được như bạn.Chờ bạn trong những hành trình tiếp theo";
				contentXday = String.format(GoogleDriveRepository.getRandomMgsAltp(), mv.getPosterName(),total);
			}
			
			System.out.println("contentXday: "+contentXday);
			System.out.println("to: "+to);
			String urlXday="";
			try {
				messengerNextLesson(driver,post_id,contentXday);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void messengerNextLesson(WebDriver driver, String posterId, String content) {
		driver.get("https://www.messenger.com/t/" + posterId);
		try {
			Thread.sleep(1000);
			driver.findElement(By.cssSelector("#u_0_1 button")).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.switchTo().activeElement().sendKeys(content + "\n");

	}

	public static void crawlVideo(WebDriver driver) throws UnsupportedEncodingException {
		
		try {
			MemberVideo newVideo =  FireStoreRepository.getNewVideo();
			
			if(newVideo==null)
			{
				findNewVideo(driver);
				return;
			}
			else
			{
				
				driver.get(newVideo.getUrl());
				String url = driver.getCurrentUrl();
				if(url.contains("pending"))
				{
					System.out.println("Pending post need approved");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				newVideo.setStatus("processed");
				newVideo.setPermalink(url);
				
				System.out.println(url);
				WebElement userContentWrapper = null;
				try {
					userContentWrapper = driver.findElement(By.className("userContentWrapper"));
				} catch (Exception e) {
					newVideo.setStatus("Error");
					FireStoreRepository.getFireStore().collection("Video").document(newVideo.getId()).set(newVideo);
					
					return;
				}

				WebElement userContent = userContentWrapper.findElement(By.className("userContent"));
				newVideo.setContent(userContent.getText().replaceAll("Xem bản dịch", ""));

				System.out.println(userContent.getText());
				WebElement userProfileElement = null;
				try {
					userProfileElement = userContentWrapper.findElement(By.cssSelector(".fwb a"));
				}catch(Exception e)
				{
					newVideo.setStatus("Profile Error");
					FireStoreRepository.getFireStore().collection("Video").document(newVideo.getId()).set(newVideo);
					
					return;
				}
				String name = userProfileElement.getText();
				System.out.println(name);
				newVideo.setPosterName(name);

				String facebookID = Utils.splitQuery(userProfileElement.getAttribute("ajaxify")).get("member_id");
				System.out.println(facebookID);
				newVideo.setPosterId(facebookID);
				
				Member mb = FireStoreRepository.getFireStore().collection("Member").document(facebookID).get().get().toObject(Member.class);
				
				if(mb==null)
				{
					mb = new Member();
					mb.setName(name);
					mb.setId(facebookID);
					FireStoreRepository.getFireStore().collection("Member").document(mb.getId()).set(mb);
				}
				
				
				String timespam = userContentWrapper.findElement(By.tagName("abbr")).getAttribute("data-utime");

				newVideo.setCreatedDate(Long.parseLong(timespam) * 1000);

				System.out.println(newVideo.getCreatedDate());

				String post_id = url.replaceAll("https://www.facebook.com/groups/cec.edu.vn/permalink/", "").replaceAll("/",
						"");

				MemberPost post = FireStoreRepository.getFireStore().collection("MemberPost").document("1784461175160264_"+post_id).get().get().toObject(MemberPost.class); 
//						ofy().load().type(MemberPost.class).id("1784461175160264_" + post_id).now();
				if(post!=null)
				{
					newVideo.setStatus("Inserted");
					FireStoreRepository.getFireStore().collection("Video").document(newVideo.getId()).set(newVideo);
//					ofy().save().entities(mv).now();
					
					return;
				}

				post = new MemberPost();

				post.setContent(newVideo.getContent());
				post.setCreatedDate(newVideo.getCreatedDate());
				post.setId("1784461175160264_" + post_id);
				post.setPosterId(newVideo.getPosterId());
				post.setAttachments("{type:\"video_inline\",url:\"" + newVideo.getId() + "\"   }");

				FireStoreRepository.getFireStore().collection("MemberPost").document(post.getId()).set(post);
				FireStoreRepository.getFireStore().collection("Video").document(newVideo.getId()).set(newVideo);
//				ofy().save().entities(post, mv).now();

				commentVideo(driver, newVideo, post);
				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}
}
