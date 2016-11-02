package twitter.Utilities;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import twitter.DataStructures.BigFile;

/**
 *
 * @author gap2
 */

public class FileUtilities {

    public static ArrayList<String> getDirectoryAbsolutePaths(String inputDir) {
        ArrayList<String> fileNames = new ArrayList<String>();

        File datasetDir = new File(inputDir);
        File[] files = datasetDir.listFiles();
        for (File file : files) {
            fileNames.add(file.getAbsolutePath());
        }
        Collections.sort(fileNames);

        return fileNames;
    }
    
    public static ArrayList<String> getDirectoryFileNames(String inputDir) {
        ArrayList<String> fileNames = new ArrayList<String>();

        File dir = new File(inputDir);
        File[] files = dir.listFiles();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        Collections.sort(fileNames);

        return fileNames;
    }

    public static ArrayList<String> getFileLines(String filePath) throws Exception {
        final ArrayList<String> lines = new ArrayList<String>();
        final BigFile file = new BigFile(filePath);
        final Iterator<String> iterator = file.iterator();
        while (iterator.hasNext()) {
            lines.add(iterator.next());
        }

        return lines;
    }
    public static Integer getNumberOfFileLines(String filePath) throws Exception {
    	int nOfLines=0;
        final BigFile file = new BigFile(filePath);
        final Iterator<String> iterator = file.iterator();
        while (iterator.hasNext()) {
        	nOfLines++;
        }

        return nOfLines;
    }
    public static void moveToDir(String fromPath,String toPath) {
    	try{
    		FileSystems.getDefault().getPath(fromPath);
    		Files.move(FileSystems.getDefault().getPath(fromPath), FileSystems.getDefault().getPath(toPath), StandardCopyOption.REPLACE_EXISTING);
     	}catch(Exception e){
     		e.printStackTrace();
     	}
	}
}
