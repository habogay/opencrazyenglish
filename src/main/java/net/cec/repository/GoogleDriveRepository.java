package net.cec.repository;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class GoogleDriveRepository {
	
	
	
	public static String[] comments = {};
	
	public static String[] xdays = {};
	public static String[] altps = {};
	
	static 
	{

		try {
			Document doc = Jsoup.connect("https://script.googleusercontent.com/macros/echo?user_content_key=kz3uouzkn7NzsrSTSiJUrKnOLfH5qd9YLbhodOmr06uDGLQsGmw2XVUbRjRcvk_GnHghBOKYrg0R1r21g9jhHuL2uoTPnqFGm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnCT0QRJ7P_-LtV3tAd8_b_dUnbO1rEvbeLLB2eAoIGhp1hENMaacOI9TktsviLkDHJlUq1JAmpDs&lib=MmSKrXssQcdpiSXxZX7nm1QZVzjmXS3D2")
		 .get();
			comments = doc.select("comment").eachText().toArray(new String[doc.select("comment").size()]);
			xdays = doc.select("msgxday").eachText().toArray(new String[doc.select("msgxday").size()]);
			altps = doc.select("msgaltp").eachText().toArray(new String[doc.select("msgaltp").size()]);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String getRandomComment()
	{
		int idx = new Random().nextInt(comments.length);
		return comments[idx];
	}
	
	public static String getRandomMgsXdays()
	{
		int idXdays = new Random().nextInt(xdays.length);
		return xdays[idXdays];
	}
	
	public static String getRandomMgsAltp()
	{
		int idAltp = new Random().nextInt(altps.length);
		return altps[idAltp];
	}
	

	
}
