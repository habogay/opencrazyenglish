package net.cec.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FireStoreRepository {

	static Firestore db = null;
	static {
		try {
			FileInputStream serviceAccount = null;

			serviceAccount = new FileInputStream("/home/habogay/backup/opencec-f66d08030f45.json");

			FirebaseOptions options = null;

			options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://opencec.firebaseio.com/").build();

			FirebaseApp.initializeApp(options);

			db = FirestoreClient.getFirestore();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Critical error!!!");
			System.exit(-1);
		}

	}
	
	public static Firestore getFireStore()
	{
		return db;
	}

	public static void main(String[] args) {
			
	}

}
