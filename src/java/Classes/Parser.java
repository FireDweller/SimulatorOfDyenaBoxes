
package Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Reads data recorded by VMMS from a *.csv data file and 
 * assigns relevant data bits to the fields of <code>Line</code> object.
 *
 * @author Georgios Fragkos 
 * @author Vilius Plepys
 * @author Ian Weeks
 */
public class Parser 
{      
    DateTimeFormatter form = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss.SSS");
    private BufferedReader reader;
    private File file;
   
     /**
     * Constructor. Creates a <code>BufferedReader</code> instance to read from a specified file.
     * Reads the first line from the file so that to omit it, as it is a header in the data file.
     * 
     * @param file path to a *.csv file
     * @throws FileNotFoundException if a file was not found on the specified file path
     * @throws IOException when failed or interrupted I/O operations occurs
     */
    public Parser(File file) throws FileNotFoundException, IOException
    {
        this.file = file;
        
        reader = new BufferedReader(new FileReader(file));
        //ignoring the first line in the file
        reader.readLine();
     }

    /**
     * Reads the next line from a file. If it is the end of file, reads the second line, 
     * because the first is the header.
     * 
     * @return Line object
     * @throws IOException when failed or interrupted I/O operations occur
     */
    public Line getSingleLine() throws IOException
    {
        String nextLine = reader.readLine();
        
        if (nextLine != null)
        {
            return new Line(nextLine.split(","));
        }      
 
        //if reader got into EOF close the buffered reader and create it again
        else 
        {
        //close the buffered imput stream  
        reader.close();     
        
        //create a buffered reader again
        reader = new BufferedReader(new FileReader(file));   
       
        //skipping the first line of the file, as it is a header
        reader.readLine();
        
        //defining nextLine again, as reader is another instance allready
        nextLine = reader.readLine();
           
        // return the next line
        return new Line(nextLine.split(","));         
        }      
    }
    
    /**
     * A <code>Line</code> instance keeps data of a single line from a *.csv file.
     */
    public class Line
    {
        private DateTime dateTime;
        private final float heave;
        private final float pitch;
        private final float roll;
        private final float heading;
        private final float heaveMin;
        private final float pitchMin;
        private final float rollMin;
        private final float heaveMax;
        private final float pitchMax;
        private final float rollMax;
        private final float accelerationX;
        private final float accelerationY;
        private final float accelerationZ;
        private final float heaveRMS;
        private final float pitchRMS;
        private final float rollRMS;
        private final float heaveTz;
        private final float pitchTz;
        private final float rollTz;
        private final float withinPresetLimits;
        private final float transferring;
        private final float latitude;
        private final float Longitude;

        /**
         * Constructor.
         * 
         * @param stringLine an array of strings, each representing a data unit
         * of a single line from a *.csv file.
         */
        public Line(String[] stringLine)
        {
            /*adds current date and time, becouse the ones in the *.csv file are 
            irrelevant for the simulation*/
            this.dateTime = form.parseDateTime(stringLine[0] + " " + stringLine[1]);
           
            this.heave = Float.valueOf(stringLine[2]);
            this.pitch = Float.valueOf(stringLine[3]);
            this.roll = Float.valueOf(stringLine[4]);
            this.heading = Float.valueOf(stringLine[5]);
            this.heaveMin = Float.valueOf(stringLine[6]);
            this.pitchMin = Float.valueOf(stringLine[7]);
            this.rollMin = Float.valueOf(stringLine[8]);
            this.heaveMax = Float.valueOf(stringLine[9]);
            this.pitchMax = Float.valueOf(stringLine[10]);
            this.rollMax = Float.valueOf(stringLine[11]);
            this.accelerationX = Float.valueOf(stringLine[12]);
            this.accelerationY = Float.valueOf(stringLine[13]);
            this.accelerationZ = Float.valueOf(stringLine[14]);
            this.heaveRMS = Float.valueOf(stringLine[15]);
            this.pitchRMS = Float.valueOf(stringLine[16]);
            this.rollRMS = Float.valueOf(stringLine[17]);
            this.heaveTz = Float.valueOf(stringLine[18]);
            this.pitchTz = Float.valueOf(stringLine[19]);
            this.rollTz = Float.valueOf(stringLine[20]);
            this.withinPresetLimits = Float.valueOf(stringLine[21]);
            this.transferring = Float.valueOf(stringLine[22]);
            this.latitude = Float.valueOf(stringLine[23]);
            this.Longitude = Float.valueOf(stringLine[24]);
        }

           /**
           * Gets date and time of the creation of a <code>Line</code> instance.
           * @return Joda <code>DateTime</code> object
           */
        public DateTime getDateTime()
        {
            return dateTime;
        }

          /**
           * Gets the <code>heave</code> of this <code>Line</code>.
           * @return  float value of the <code>heave</code> 
           */
        public float getHeave()
        {
            return heave;
        }

          /**
           * Get the <code>pitch</code> of this <code>Line</code>.
           * @return float value of the <code>pitch</code> 
           */
        public float getPitch()
        {
            return pitch;
        }

          /**
           * Get the <code>roll</code> of this <code>Line</code>.
           * @return float value of the <code>roll</code> 
           */
        public float getRoll()
        {
            return roll;
        }

          /**
            * Get the <code>heading</code> of this <code>Line</code>.
            * @return float value of the <code>Heading</code> 
           */
        public float getHeading()
        {
            return heading;
        }

          /**
            * Get the <code>heaveMin</code> of this <code>Line</code>.
            * @return float value of the <code>heaveMin</code> 
           */
        public float getHeaveMin()
        {
            return heaveMin;
        }

           /**
            * Get the <code>pitchMin</code> of this <code>Line</code>.
            * @return float value of the <code>pitchMin</code> 
           */
        public float getPitchMin()
        {
            return pitchMin;
        }

          /**
            * Get the <code>rollMin</code> of this <code>Line</code>.
            * @return float value of the <code>rollMin</code> 
           */
        public float getRollMin()
        {
            return rollMin;
        }

          /**
            * Get the <code>heaveMax</code> of this <code>Line</code>.
            * @return float value of the <code>heaveMax</code> 
           */
        public float getHeaveMax()
        {
            return heaveMax;
        }

          /**
            * Get the <code>pitchMax</code> of this <code>Line</code>.
            * @return float value of the <code>pitchMax</code> 
           */
        public float getPitchMax()
        {
            return pitchMax;
        }

          /**
            * Get the <code>rollMax</code> of this <code>Line</code>.
            * @return float value of the <code>rollMax</code> 
           */
        public float getRollMax()
        {
            return rollMax;
        }
            /**
            * Get the <code>accelerationX</code> of this <code>Line</code>.
            * @return float value of the <code>accelerationX</code> 
           */
        public float getAccelerationX()
        {
            return accelerationX;
        }

           /**
            * Get the <code>accelerationY</code> of this <code>Line</code>.
            * @return float value of the <code>accelerationY</code> 
           */
        public float getAccelerationY()
        {
            return accelerationY;
        }
  
        /**
            * Get the <code>accelerationZ</code> of this <code>Line</code>.
            * @return float value of the <code>accelerationZ</code> 
           */
        public float getAccelerationZ()
        {
            return accelerationZ;
        }

            /**
            * Get the <code>heaveRMS</code> of this <code>Line</code>.
            * @return float value of the <code>heaveRMS</code> 
           */
        public float getHeaveRMS()
        {
            return heaveRMS;
        }

          /**
            * Get the <code>pitchRMS</code> of this <code>Line</code>.
            * @return float value of the <code>pitchRMS</code> 
           */
        public float getPitchRMS()
        {
            return pitchRMS;
        }

          /**
            * Get the <code>rollRMS</code> of this <code>Line</code>.
            * @return float value of the <code>rollRMS</code> 
           */
        public float getRollRMS()
        {
            return rollRMS;
        }

          /**
            * Get the <code>heaveTz</code> of this <code>Line</code>.
            * @return float value of the <code>heaveTz</code> 
           */
        public float getHeaveTz()
        {
            return heaveTz;
        }

           /**
            * Get the <code>pitchTz</code> of this <code>Line</code>.
            * @return float value of the <code>pitchTz</code> 
           */
        public float getPitchTz()
        {
            return pitchTz;
        }

           /**
            * Get the <code>rollTz</code> of this <code>Line</code>.
            * @return float value of the <code>rollTz</code> 
           */
        public float getRollTz()
        {
            return rollTz;
        }
  /**
            * Get the <code>withinPresetLimits</code> value of this <code>Line</code>.
            * @return float value of the <code>withinPresetLimits</code> 
           */
        public float getWithinPresetLimits()
        {
            return withinPresetLimits;
        }

           /**
            * Get the <code>transferring</code> of this <code>Line</code>.
            * @return float value of the <code>transferring</code> 
           */
        public float getTransferring()
        {
            return transferring;
        }
           /**
            * Get the <code>latitude</code> of this <code>Line</code>.
            * @return float value of the <code>latitude</code> 
           */
        public float getLatitude()
        {
            return latitude;
        }

          /**
            * Get the <code>longitude</code> of this <code>Line</code>.
            * @return float value of the <code>longitude</code> 
           */
        public float getLongitude()
        {
            return Longitude;
        }

        @Override
        public String toString()
        {
            return "Line{" + "dateTime=" + dateTime + ", heave=" + heave + ", "
                    + "\n pitch=" + pitch + ", roll=" + roll + ", "
                    + "\n heading=" + heading + ", heaveMin=" + heaveMin + ", "
                    + "\n pitchMin=" + pitchMin + ", r ollMin=" + rollMin + ", "
                    + "\n heaveMax=" + heaveMax + ", pitchMax=" + pitchMax + ", "
                    + "\n rollMax=" + rollMax + ", "
                    + "\n accelerationX=" + accelerationX + ", "
                    + "\n accelerationY=" + accelerationY + ", "
                    + "\n accelerationZ=" + accelerationZ + ", "
                    + "\n heaveRMS=" + heaveRMS + ", pitchRMS=" + pitchRMS + ", "
                    + "\n rollRMS=" + rollRMS + ", heaveTz=" + heaveTz + ", "
                    + "\n pitchTz=" + pitchTz + ", rollTz=" + rollTz + ", "
                    + "\n withinPresetLimits=" + withinPresetLimits + ", "
                    + "\n transferring=" + transferring + ", "
                    + "\n latitude=" + latitude + ", Longitude=" + Longitude + '}';
        }

    }

}
