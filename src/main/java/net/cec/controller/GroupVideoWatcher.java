package net.cec.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import net.cec.Secret;
import net.cec.entities.MemberVideo;
import net.cec.repository.FireStoreRepository;

public class GroupVideoWatcher extends Thread {

	static Logger logger = Logger.getLogger(GroupVideoWatcher.class.getName());

	public static void main(String[] args) {

		String urlFB = "http://facebook.com";
		String email = Secret.email;
		String password = Secret.password;
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		
		ChromeOptions options = new ChromeOptions();
		
		options.setExperimentalOption("prefs", prefs);
		WebDriver driver = new ChromeDriver(options);

		driver.get(urlFB);

		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("pass")).sendKeys(password + Keys.ENTER);
		
		
		findNewVideo(driver);
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
	
}
