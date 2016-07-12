package Classes;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Keeps a list of *.csv files contained in a specified
 * directory. Controls accessibility to those files, so that no 2 <code>DyenaBox</code>
 * instances would read from one file.
 *
 * @author Vilius Plepys
 */
public class DataFileHandler {

    private File[] files = null; // all files in disposition
    private Boolean[] availability; // to show if a file is available to be used
    private boolean noFilesFound = false; // to show if files were found in the directory
    
 
 
    /**
     * Zero argument constructor
     */
    public DataFileHandler() {
    }

    /**
     * Finds *.csv files in a directory and saving their absolute paths in <code>File</code> instances.
     *
     * @param directoryName absolute path of a directory where the *.csv files
     * are held
     */
    
       /**
     * Checks if a file is available to be used.
     * 
     * @param i index of a file
     * @return boolean, true for available
     */
    public Boolean isAvailable(int i) {
        return this.availability[i];
    }

    /**
     * Set availability of a file. 
     * 
     * @param i Index of the file in <code>File</code> array.
     * @param tOf boolean, true for available
     */
    public void setAvailability(int i, Boolean tOf) {
        this.availability[i] = tOf;
    }
   
    
    /**
     * Checks if there are any more files available to be used, besides the ones
     * that already are used.
     * 
     * @return boolean, true if there are files available to be used
     */
    public Boolean areAnyMoreFilesAvailable()
    {
        for (int i=0; i<availability.length; i++)
        {
           if (availability[i].booleanValue()==Boolean.TRUE)
           return Boolean.TRUE;     
         }
        return Boolean.FALSE;
    }
    
    /**
     * Looks for files with extension *.csv in the given directory.
     * 
     * @param directoryName absolute path to directory holding the data files 
     */
    public void lookupForFiles(String directoryName) {

        //Creating a filter to filter out *.csv files
        FilenameFilter csvFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".csv");
            }
        };

        File directory = new File(directoryName);
        //getting an array of all files in the directory 
       
        this.files = directory.listFiles(csvFilter);
      
        if (this.files != null){
        
            //remove any files from fileList 
            for (File file : this.files) {
                if (file.isDirectory()) {
                    file.delete();
                }
            }
            
            
            this.availability = new Boolean[this.files.length];  
           
            for (int i=0; i<this.availability.length;i++)
            {
                this.availability[i]=true;
            }
        }
        
        else
        
        {
            this.noFilesFound = true;
        }
       
    }

    
    /**
     * Get array of all <code>File</code> instances.
     *
     * @return The file array.
     */
    public File[] getFiles() {
        return this.files;
    }

    
    /**
     * Get <code>File</code> instance.
     * 
     * @param i index of <code>File</code> instance in file array
     * @return <code>File</code> instance
     */
    public File getFile(int i) {
        return this.files[i];
    }

    /**
     * Gets all the names of the data files in disposition. 
     * @return array of names of files
     */
    public String[] getFileNames() {  
        
        String[] fileNames = new String[this.files.length]; 
        for (int i = 0; i < this.files.length; i++) {
            fileNames[i] = this.files[i].getName();
        }   
        return fileNames;
    }
    
    /**
     * Checks if there are files found at all.
     * @return boolean, true if found 
     */
    public boolean areFilesFound()
    {
        return !this.noFilesFound;
    }

}
