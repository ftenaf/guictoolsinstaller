package net.juantxu.ctools.intaller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

public class Descargador {

	public boolean update(String pentahoSolutionsDir, String tomcatDir,
			Boolean CDF, Boolean CDA, Boolean CDE, Boolean CGG,
			Boolean saikuDev, Boolean saikuStable) {
		boolean exito = false;
		Properties prop = new CargaProperties().Carga();
		if (CDF)
			exito = descargaWin(prop.getProperty("CDF"), pentahoSolutionsDir, tomcatDir);
		if (CDA)
			exito = descargaWin(prop.getProperty("CDA"), pentahoSolutionsDir,
					tomcatDir);
		if (CDE)
			exito = descargaWin(prop.getProperty("CDE"), pentahoSolutionsDir,
					tomcatDir);
		if (CGG)
			exito = descargaWin(prop.getProperty("CGG"), pentahoSolutionsDir,
					tomcatDir);
		if (saikuDev)
			exito = descargaWin(new BuscaCTools().buscaSAIKU(), pentahoSolutionsDir,
					tomcatDir);
		if (saikuStable)
			exito = descargaWin(prop.getProperty("SaikuStable"), pentahoSolutionsDir,
					tomcatDir);
		// descargaWin(prop.getProperty("CDF_SOLUTION"), pentahoSolutionsDir);
		return exito;
	}

	private boolean descargaWin(String APP, String pentahoSolutionsDir,
			String tomcatDir) {
		boolean exito = false;
		String[] arr = APP.split("/");
		String miApp = arr[arr.length - 1];
		descarga(APP, pentahoSolutionsDir + "/tmp/" + miApp);
		new UnZip(pentahoSolutionsDir + "/tmp/" + miApp);
		exito = ubica(miApp.substring(0, miApp.length() - 4), pentahoSolutionsDir,
				tomcatDir);
		return exito;
	}

	private void descarga(String origen, String destino) {
		System.out.println("downloading " + origen);
		System.out.println("in " + destino);
		URL url; // represents the location of the file we want to dl.
		URLConnection con; // represents a connection to the url we want to dl.
		DataInputStream dis; // input stream that will read data from the file.
		FileOutputStream fos; // used to write data from inut stream to file.
		byte[] fileData; // byte aray used to hold data from downloaded file.
		try {
			url = new URL(origen);
			con = url.openConnection(); // open the url connection.
			dis = new DataInputStream(con.getInputStream()); // get a data
																// stream from
																// the url
																// connection.
			fileData = new byte[con.getContentLength()]; // determine how many
															// byes the file
															// size is and make
															// array big enough
															// to hold the data
			for (int x = 0; x < fileData.length; x++) { // fill byte array with
														// bytes from the data
														// input stream
				fileData[x] = dis.readByte();
			}
			dis.close(); // close the data input stream
			fos = new FileOutputStream(new File(destino)); // create an object
															// representing the
															// file we want to
															// save
			fos.write(fileData); // write out the file we want to save.
			fos.close(); // close the output stream writer
		} catch (MalformedURLException m) {
			System.out.println(m);
		} catch (IOException io) {
			System.out.println(io);
		}

	}

	private boolean ubica(String app, String pentahoSolutionsDir, String tomcatDir) {
		boolean exito = true;
		if (app.toLowerCase().contains("cda")) {
			File cda = new File(pentahoSolutionsDir + "/tmp/" + app + "/cda");
			if (cda.exists()) {
				File cdaBK = new File(pentahoSolutionsDir + "/tmp/cdaBackUp"
						+ new Date().getTime());
				File cdaOrig = new File(pentahoSolutionsDir + "/system/cda");
				if (cdaOrig.exists()) {
					cdaOrig.renameTo(cdaBK);
				} else {
					System.out.println("Unable to find Previous cda");
				}
				if (cda.renameTo(new File(pentahoSolutionsDir + "/system/cda"))) {
					System.out.println("==================>CDA Updated");
					exito = true;
				} else {
					System.out.println("+++++++UNABLE TO UPDATE CDA+++++");
					exito = false;
				}
			} else {
				System.out.println("CDA directory is not available");
				exito = false;
			}

		} else if (app.toLowerCase().contains("pentaho-cdf-dd")) {
			File cda = new File(pentahoSolutionsDir + "/tmp/" + app
					+ "/pentaho-cdf-dd");
			if (cda.exists()) {
				File cdaBK = new File(pentahoSolutionsDir
						+ "/tmp/pentaho-cdf-dd-BackUp" + new Date().getTime());
				File cdaOrig = new File(pentahoSolutionsDir
						+ "/system/pentaho-cdf-dd");
				if (cdaOrig.exists()) {
					cdaOrig.renameTo(cdaBK);
				} else {
					System.out
							.println("Unable to find Previous pentaho-cdf-dd");
				}
				if (cda.renameTo(new File(pentahoSolutionsDir
						+ "/system/pentaho-cdf-dd"))) {
					System.out.println("==================>pentaho-cdf-dd Updated");
					exito = true;
				} else {
					System.out
							.println("+++++++UNABLE TO UPDATE pentaho-cdf-dd+++++");
					exito = false;
				}
			} else {
				System.out.println("pentaho-cdf-dd directory is not available");
				exito = false;
			}
		} else if (app.toLowerCase().contains("cgg")) {
			File cgg = new File(pentahoSolutionsDir + "/tmp/" + app + "/cgg");
			if (cgg.exists()) {
				File cggBK = new File(pentahoSolutionsDir + "/tmp/cgg-BackUp"
						+ new Date().getTime());
				File cggOrig = new File(pentahoSolutionsDir + "/system/cgg");
				if (cggOrig.exists()) {
					cggOrig.renameTo(cggBK);
				} else {
					System.out.println("Unable to find Previous cgg");
				}
				if (cgg.renameTo(new File(pentahoSolutionsDir + "/system/cgg"))) {
					updateCGGJars(pentahoSolutionsDir + "/system/cgg/lib", tomcatDir);
					System.out.println("==================>cgg Updated");
					exito = true;
				} else {
					System.out.println("+++++++UNABLE TO UPDATE cgg+++++");
					exito = false;
				}

				

			} else {
				System.out.println("cgg directory is not available");
				exito = false;
			}
		} else if (app.toLowerCase().contains("pentaho-cdf")) {
			File cda = new File(pentahoSolutionsDir + "/tmp/" + app
					+ "/pentaho-cdf");
			if (cda.exists()) {
				File cdaBK = new File(pentahoSolutionsDir
						+ "/tmp/pentaho-cdf-BackUp" + new Date().getTime());
				File cdaOrig = new File(pentahoSolutionsDir
						+ "/system/pentaho-cdf");
				if (cdaOrig.exists()) {
					cdaOrig.renameTo(cdaBK);
				} else {
					System.out.println("Unable to find Previous pentaho-cdf");
				}
				if (cda.renameTo(new File(pentahoSolutionsDir
						+ "/system/pentaho-cdf"))) {
					System.out.println("==================>pentaho-cdf Updated");
					exito = true;
				} else {
					System.out
							.println("+++++++UNABLE TO UPDATE pentaho-cdf+++++");
					exito = false;
				}
			} else {
				System.out.println("pentaho-cdf directory is not available");
				exito = false;
			}

		} else if (app.toLowerCase().contains("saiku")) {
			File cda = new File(pentahoSolutionsDir + "/tmp/" + app + "/saiku");
			if (cda.exists()) {
				File cdaBK = new File(pentahoSolutionsDir + "/tmp/saiku-BackUp"
						+ new Date().getTime());
				File cdaOrig = new File(pentahoSolutionsDir + "/system/saiku");
				if (cdaOrig.exists()) {
					cdaOrig.renameTo(cdaBK);
				} else {
					System.out.println("Unable to find Previous saiku");
				}
				if (cda.renameTo(new File(pentahoSolutionsDir + "/system/saiku"))) {
					System.out.println("==================>saiku Updated");
					exito = true;
				} else {
					System.out.println("+++++++UNABLE TO UPDATE saiku+++++");
					exito = false;
				}
			} else {
				System.out.println("saiku directory is not available");
				exito = false;
			}

		}
		return exito;
	}

	private void updateCGGJars(String cggJars, String tomcatDir) {
		deletePreviousCGGJars(tomcatDir);
		copyCGGJars(cggJars, tomcatDir);
	
	}
	private void copyCGGJars(String cggJars, String tomcatDir) {

		MyFileFilter filter = new MyFileFilter("batik-");
		File dir = new File(cggJars);
		String[] list = dir.list(filter);
		File file, dest;
		if (list.length == 0) {
			System.out.println("no jars available to copy");
			return;
		}
		for (int i = 0; i < list.length; i++) {
			// file = new File(directory + list[i]);
			file = new File(dir, list[i]);
			if (! list[i].contains("batik-js")){
			dest = new File(tomcatDir + "/webapps/pentaho/WEB-INF/lib/" +list[i] ); 
				new CopyFile(file, dest);
			}
		}
	}
	private void deletePreviousCGGJars(String tomcatDir){
		
		// Deleting batik-
		MyFileFilter filter = new MyFileFilter("batik-");
		File dir = new File(tomcatDir + "/webapps/pentaho/WEB-INF/lib");
		String[] list = dir.list(filter);
		File file;
		if (list.length == 0) {
			System.out.println("no previos files in WEB-INF/lib");
			return;
		}
		for (int i = 0; i < list.length; i++) {
			// file = new File(directory + list[i]);
			file = new File(dir, list[i]);
			System.out.print(file + "  deleted : " + file.delete());
		}
		
		// Deleting xml-apis
		filter = new MyFileFilter("xml-apis");
		list = dir.list(filter);
		if (list.length == 0) {
			System.out.println("no previos files in WEB-INF/lib");
			return;
		}
		for (int i = 0; i < list.length; i++) {
			// file = new File(directory + list[i]);
			file = new File(dir, list[i]);
			 file.delete();
		}
		
		// Deleting xmlgraphics
		filter = new MyFileFilter("xmlgraphics");
		list = dir.list(filter);
		if (list.length == 0) {
			System.out.println("no previos files in WEB-INF/lib");
			return;
		}
		for (int i = 0; i < list.length; i++) {
			// file = new File(directory + list[i]);
			file = new File(dir, list[i]);
			file.delete();
		}
		
	}
}