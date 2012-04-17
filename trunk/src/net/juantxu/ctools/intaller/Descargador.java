package net.juantxu.ctools.intaller;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

public class Descargador {

	public boolean update(String pentahoSolutionsDir, String tomcatDir,
			Boolean CDF, Boolean CDA, Boolean CDE, Boolean SAMPLES, Boolean CGG,
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
		if (SAMPLES)
			exito = descargaSamples(prop.getProperty("CDA_SAMPLES"),
									prop.getProperty("CDE_SAMPLES"),
									prop.getProperty("CDF_SAMPLES"),
									pentahoSolutionsDir,
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

	private boolean descargaSamples(String cda_samples, 
									String cde_samples,
									String cdf_samples, 
									String pentahoSolutionsDir, 
									String tomcatDir) {
		boolean exito = false;
		//Creating plugin-samples directory 
		System.out.println("Deleting plugin-samples folder under pertaho-solutions");		
		File f = new File( pentahoSolutionsDir + "/plugin-samples/");
		new DeleteDir().deleteDirectory(f);
		f.mkdir();
		//Creating index.xml
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(pentahoSolutionsDir + "/plugin-samples/index.xml");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write("<index><visible>true</visible><name>Plugin Samples</name><description>Plugin Samples</description></index>");
			  //Close the output stream
			  out.close();
			  exito=true;
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  exito=false;
			  }
		
		//downloading samples
		exito = descargaWin(cda_samples, pentahoSolutionsDir,
						tomcatDir);
		exito = descargaWin(cde_samples, pentahoSolutionsDir,
				tomcatDir);
		
		exito = descargaWin(cdf_samples, pentahoSolutionsDir,
				tomcatDir);
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
		if ( (app.toLowerCase().contains("cda")) && !(app.toLowerCase().contains("sample") ) ) {
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

		} else if ((app.toLowerCase().contains("pentaho-cdf-dd"))&& !(app.toLowerCase().contains("solution") ) ) {
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
		} else if ((app.toLowerCase().contains("pentaho-cdf")) && !(app.toLowerCase().contains("sample") ) && !(app.toLowerCase().contains("solution") )  ) {
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
			
		// samples 	
		} else if (app.toLowerCase().contains("cda-samples")) {
			File cdasamples = new File(pentahoSolutionsDir + "/tmp/" + app
					+ "/cda");
			if (cdasamples.exists()) {
				if (cdasamples.renameTo(new File(pentahoSolutionsDir
						+ "/plugin-samples/cda"))) {
					System.out.println("==================>cda-samplese Updated");
					exito = true;
				} else {
					System.out
							.println("+++++++UNABLE TO UPDATE cda-samplese+++++");
					exito = false;
				}
			} else {
				System.out.println("cda-samplese directory is not available");
				exito = false;
			}
		} else if (app.toLowerCase().contains("pentaho-cdf-dd-solution")) {
			File cdfddsamples = new File(pentahoSolutionsDir + "/tmp/" + app
					+ "/pentaho-cdf-dd");
			if (cdfddsamples.exists()) {
				if (cdfddsamples.renameTo(new File(pentahoSolutionsDir
						+ "/plugin-samples/pentaho-cdf-dd"))) {
					System.out.println("==================>cdf-dd-samples Updated");
					exito = true;
				} else {
					System.out
							.println("+++++++UNABLE TO UPDATE cdf-dd-samplese+++++");
					exito = false;
				}
			} else {
				System.out.println("cdfdd-samplese directory is not available");
				exito = false;
			}
		} else if (app.toLowerCase().contains("pentaho-cdf-samples")) {
			File cdfsamples = new File(pentahoSolutionsDir + "/tmp/" + app
					+ "/pentaho-cdf");
			if (cdfsamples.exists()) {
				if (cdfsamples.renameTo(new File(pentahoSolutionsDir
						+ "/plugin-samples/pentaho-cdf"))) {
					System.out.println("==================>cdf-samples Updated");
					exito = true;
				} else {
					System.out
							.println("+++++++UNABLE TO UPDATE cdf-samplese+++++");
					exito = false;
				}
			} else {
				System.out.println("cda-samplese directory is not available");
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