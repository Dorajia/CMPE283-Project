package com.EXSI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
public class unTarOva {
	    
	    /**
	     * Extract all files from Tar into the specified directory
	     * 
	     * @param tarFile
	     * @param directory
	     * @return the list of extracted filenames
	     * @throws IOException
	     */
	    public static List<String> unTar(String tarFilePath, String directoryPath) throws IOException {
	        List<String> result = new ArrayList<String>();
			File tarFile= new File(tarFilePath);
			File directory = new File(directoryPath);
	        InputStream inputStream = new FileInputStream(tarFile);
	        TarArchiveInputStream in = new TarArchiveInputStream(inputStream);
	        TarArchiveEntry entry = in.getNextTarEntry();
	        while (entry != null) {
	            if (entry.isDirectory()) {
	                entry = in.getNextTarEntry();
	                continue;
	            }
	            File curfile = new File(directory, entry.getName());
	            File parent = curfile.getParentFile();
	            if (!parent.exists()) {
	                parent.mkdirs();
	            }
	            OutputStream out = new FileOutputStream(curfile);
	            IOUtils.copy(in, out);
	            out.close();
	            result.add(entry.getName());
	            entry = in.getNextTarEntry();
	        }
	        in.close();
	        return result;
	    }
}
