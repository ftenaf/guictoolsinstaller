package net.juantxu.ctools.intaller;

import java.io.File;
import java.io.FilenameFilter;


public class MyFileFilter implements FilenameFilter {
	  private String extension;
	  public MyFileFilter( String extension ) {
	    this.extension = extension;             
	  }
	  
	  public boolean accept(File dir, String name) {
	    return (name.contains(extension));
	  }
	}
