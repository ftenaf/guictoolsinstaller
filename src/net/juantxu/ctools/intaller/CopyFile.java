package net.juantxu.ctools.intaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFile {

	public CopyFile(File input, File output) {
		InputStream in = null;
		OutputStream out = null;
		byte[] buf = new byte[1024];
		int len;
		try {
			in = new FileInputStream(input);
			out = new FileOutputStream(output, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				//System.out.println(output +" created ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}