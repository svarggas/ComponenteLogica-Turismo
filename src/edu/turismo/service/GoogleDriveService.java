package edu.turismo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import edu.turismo.model.LugarTuristico;

public class GoogleDriveService {

	private static final String APPLICATION_NAME = "Componentes";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
	private static final String CREDENTIALS_FILE_PATH = "/resources/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	private static Drive getDriveService() throws GeneralSecurityException, IOException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		return driveService;
	}

	public LugarTuristico uploadImage(LugarTuristico lugarTuristico, java.io.File filePath)
			throws GeneralSecurityException, IOException {
		File fileMetadata = new File();
		fileMetadata.setName(lugarTuristico.getId() + " - " + lugarTuristico.getNombre() + ".jpg");

		FileContent mediaContent = new FileContent("image/jpeg", filePath);
		File file;

		Drive driveService = getDriveService();

		if ((lugarTuristico.getIdImagen() == null) || (lugarTuristico.getIdImagen().isEmpty())) {
			mediaContent = new FileContent("image/jpeg", filePath);
			file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();

			Conector conector = new Conector();
			conector.startEntityManagerFactory();

			LugarTuristico lt = conector.getEm().find(LugarTuristico.class, lugarTuristico.getId());
			lt.setIdImagen(file.getId());

			conector.getEm().getTransaction().begin();
			conector.getEm().persist(lt);
			conector.getEm().flush();
			conector.getEm().getTransaction().commit();
			conector.stopEntityManagerFactory();

			lugarTuristico = lt;
		} else {
			driveService.files().update(lugarTuristico.getIdImagen(), fileMetadata, mediaContent).execute();
		}

		return lugarTuristico;
	}

	public InputStream getImage(LugarTuristico lugarTuristico) throws IOException, GeneralSecurityException {
		Drive driveService = getDriveService();

		InputStream inputStream = driveService.files().get(lugarTuristico.getIdImagen()).executeMediaAsInputStream();

		return inputStream;
	}
}
