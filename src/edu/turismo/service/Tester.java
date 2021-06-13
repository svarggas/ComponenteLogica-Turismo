package edu.turismo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import edu.turismo.model.LugarTuristico;

public class Tester {

	public static void main(String[] args) throws IOException {

		TurismoLugares turismoLugares = new TurismoLugares();

		List<LugarTuristico> lugares = turismoLugares.getLugaresTuristicos("Costa Rica");
		if ((lugares != null) && !lugares.isEmpty()) {
			for (LugarTuristico l : lugares) {
				System.out.println(l.getId() + " | " + l.getNombre() + " | " + l.getCiudad().getNombre());
			}
			System.out.println(new File("/Users/josepabloramirez/imagesComponentes/‎⁨⁩‎⁨wallpaper.jpg").exists());
			// uploadTest(lugares.get(1));
			// downloadTest(lugares.get(1));
		} else {
			System.out.println("Empty!");
		}

//		TurismoLugares tl = new TurismoLugares();
//		List<LugarTuristico> lugares = tl.getLugaresTuristicos("Costa Rica");
//		if ((lugares != null) && !lugares.isEmpty()) {
//			for (LugarTuristico l : lugares) {
//				System.out.println(l.getId() + " | " + l.getNombre() + " | " + l.getCiudad().getNombre());
//			}
//		} else {
//			System.out.println("Empty!");
//		}
//
//		tl.agregarlugares("Costa Rica", "Guanacaste", "Hotel Riu", "12");
//		tl.agregarLugarConPais(1, "Cartago", "Ruinas", "123");
//		tl.agregarLugarConCiudad(6, "Castillo Country Club", "1234");

	}

//	public static void uploadTest(LugarTuristico lugarTuristico) throws FileNotFoundException {
//		try {
//			GoogleDriveService.uploadImage(lugarTuristico,
//					new File("/Users/josepabloramirez/imagesComponentes/wallpaper.jpg"));
//		} catch (GeneralSecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void downloadTest(LugarTuristico lugarTuristico) {
//		try {
//			InputStream inputStream = GoogleDriveService.getImage(lugarTuristico);
//			OutputStream outputStream = new FileOutputStream("/Users/josepabloramirez/imagesComponentes/‎⁨⁩‎⁨wallpaper2.jpg");
//			inputStream.transferTo(outputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (GeneralSecurityException e) {
//			e.printStackTrace();
//		}
//	}

}
