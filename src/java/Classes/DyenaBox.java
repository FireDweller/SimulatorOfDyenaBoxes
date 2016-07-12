package Classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class representing a Dyena box entity. It creates a new <code>Message</code> instance every time <code>DyenaBoxHandler
 * </code> instance activates it.  <code>DyenaBoxHandler</code> accesses that object from this class.
 * <p>
 * The class receives data from the <code>Parser</code>, which reads it from a data file. Then it uses the data to compute
 * some values: vessel's speed over ground, course over ground, engine's resolutions per minute, fuel consumption,
 * throttle, also forms arrays of 25 values of vessel's heave, pitch, roll, heading, accelerations on x, y and z axes
 * and passes the newly computed and read values to the <code>Message</code> constructor. 
 * <p>
 * This class can have a data file assigned, from where the <code>Parser</code> instance reads it.
 * 
 * @author Vilius Plepys and Ian Weeks
 */
public class DyenaBox implements Runnable {

    private boolean simulated = false; // is the DyenaBox simulated?
    private boolean blackout = false; // is the DyeanBox in blackout
    private boolean fileAssigned = false; // is file assigned?
    private final int boxId; //ID of this DyenaBox
    private Parser parser;
    private Message localMessage = new Message();
    private int assignedDataFileIndex; //the index of the file assigned to this DyenaBox 
  
    File dataFile = null;

    private SOG sog = new SOG(); //speed over ground
    private COG cog = new COG(); // course over ground
    private int rpm = 0; //revolutions per minute
    private double fph = 0; // litres of fuel per hour
    private int th = 0; // throttle

    /**
     * Constructor
     * 
     * @param boxId the ID of a Dyena box entity 
     */
    public DyenaBox(int boxId) {
        this.boxId = boxId + 1;// +1 so that the box's ids would be from 1, not 0
    }

    /**
    * Checks if a file is assigned to this <code>DyenaBox</code> instance
    * 
    * @return true if file is assigned
    */
    public boolean isFileAssigned() {
        return fileAssigned;
    }

    /**
     * Sets true if a file is assigned, or false if it is not
     * 
     * @param fileAssigned - boolean, which holds true for file assigned state, and false for file not assigned.
     */
    public void setFileAssigned(boolean fileAssigned) {
        this.fileAssigned = fileAssigned;
    }

    /**
     * Clears the current <code>Message</code> instance by creating a new 
     * instance with the same name
     */
    public void clearMessage() {
        this.localMessage = new Message();
    }

    /**
     * Checks if this <code>DyenaBox</code> object is in a blackout state
     * 
     * @return true if in blackout state
     */
    public boolean isBlackout() {
        return blackout;
    }

    /**
     * Sets blackout state of this object to true or false
     * 
     * @param blackout - boolean
     */
    public void setBlackout(boolean blackout) {
        this.blackout = blackout;
    }

    /**
     * Gets data file index of the file assigned to this object
     * 
     * @return index of the data file assigned
     */
    public int getAssignedDataFileIndex() {
        return assignedDataFileIndex;
    }

    /**
     * Sets an index for the data file assigned to this object
     * 
     * @param assignedDataFileIndex  - index
     */
    
    public void setAssignedDataFileIndex(int assignedDataFileIndex) {
        this.assignedDataFileIndex = assignedDataFileIndex;
    }

    /**
     * Assigns a data file to this object
     * 
     * @param dataFile - absolute path to the data file
     * @throws IOException if file is not found 
     */
    public void assignDataFile(File dataFile) throws IOException {
        this.dataFile = dataFile;
        this.parser = new Parser(dataFile);
    }

    /**
     * Checks if this object is in simulation
     * 
     * @return true if in simulation
     */
    public boolean isSimulated() {
        return this.simulated;
    }

    /**
     * Sets simulation status for this object
     * 
     * @param status - boolean
     */
    public void setSimulationStatus(Boolean status) {
        this.simulated = status;
    }

    /**
     * Gets the data file of this object
     * 
     * @return <code>File</code> object 
     */
    public File getDataFile() {
        return dataFile;
    }

    /**
     * Gets the current <code>Message</code> instance from this object
     * 
     * @return <code>Message</code> instance
     */
    
    public synchronized Message getMessage() {
        return localMessage;
    }

    /**
     * Executes a thread in which parameters are gathered and calculated for
     * the creation of a new <code>Message</code> instance
     */
    @Override
    public void run() {
        double[] heave, pitch, roll, heading, ax, ay, az;
        heave = new double[25];
        pitch = new double[25];
        roll = new double[25];
        heading = new double[25];
        ax = new double[25];
        ay = new double[25];
        az = new double[25];

        try {
            Parser.Line line = null;

            //Fill the arrays.  25 values per 1 second;
            for (int i = 0; i < 25; i++) {
                line = parser.getSingleLine();

                heave[i] = line.getHeave();
                pitch[i] = line.getPitch();
                roll[i] = line.getRoll();
                heading[i] = line.getHeading();
                ax[i] = line.getAccelerationX();
                ay[i] = line.getAccelerationY();
                az[i] = line.getAccelerationZ();
            }

            synchronized (this) {
                sog.setLocation(line.getLatitude(), line.getLongitude());
                
                cog.setParameters(line.getLatitude(), line.getLongitude(), sog.getSOG());
                           
                rpm = (int)getRPM(sog.getSOG());
                
                fph = getFPH(rpm);
                fph = round(fph,2);
                //adjusting the value of fph to the resolution of 0.05 as in the specification
                //if the 2nd decimal number is < than 5, round it to 5
               if (((fph * 10 - (int)(fph * 10)) < 0.5) && ((fph * 10 - (int)(fph * 10)) > 0)){
                    fph = (int)(fph * 10);
                    fph = (fph + 0.5)/10;
                }
                //if the 2nd decimal number is > than 5, round it to 10
              else  if ((fph * 10 - (int)(fph * 10)) > 0.5){
                    fph = (int)(fph * 10 + 1);
                    fph = fph/10;
                }
                
                th = getTh(rpm);
               
                localMessage = new Message(line, this.boxId, heave, pitch, roll, heading, ax, ay, az, sog.getSOG(), cog.getCOG(), rpm, fph, th);
            }

        } catch (IOException ex) {
        }
    }
    
     /**
     * Get an RPM value for a given speed.
     * (Code from Ian Week's WMMS parser program)
     *
     * @param speed The given speed.
     * @return the RPM value for the given speed
     */
    public double getRPM(double speed)
    {
        if (speed < 6.1)
        {
            return 42.29 * speed + 650;
        }else if (speed <= 10.8)
        {
            return 62.97 * speed + 523.8;
        }else if (speed < 13.5)
        {
            return 71.48 * speed + 432;
        }else if (speed < 15.1)
        {
            return 128.1 * speed - 332.6;
        }else if (speed < 19.3)
        {
            return 45.95 * speed + 908.1;
        }else if (speed < 23.9)
        {
            return 38.47 * speed + 1052;
        }else
        {
            return 19.03 * speed + 1517;
        }
    }
    
     /**
     * Gets the fuel usage per hour value for the given RPM. 
     * (Code from Ian Week's WMMS parser program)
     *
     * @param rpm The given RPM;
     * @return The fuel usage per hour for the RPM
     */
    public double getFPH(int rpm)
    {
        if (rpm <= 825)
        {
            return 5;
        }
        else if (rpm <= 1204)
        {
            return 0.128 * rpm - 105.2;
        }
        else if (rpm < 1397)
        {
            return 0.268 * rpm - 273.4;
        }
        else if (rpm <= 1602)
        {
            return 0.567 * rpm - 691.0;
        }
        else if (rpm < 1795)
        {
            return 0.643 * rpm - 813.1;
        }
        else if (rpm <= 1972)
        {
            return 0.333 * rpm - 256.3;
        }else
        {
            return 1.444 * rpm - 2447;
        }
    }
    
    /**
     * Gets the throttle value for given <code>rpm</code> (resolutions per minute) value.
     * 
     * @param rpm - resolutions per minute
     * @return throttle value for given <code>rpm</code>
     */
    public int getTh(int rpm)
    {      
        //throttle = 0 when rpm = 800. The maximum rpm value is considered to be 2200.
        if (rpm <= 800){return 0;}
        else return ((rpm - 800) * 100)/1400 ;
    }
    
    /*-----COG:beginning----------------------------------------------------------------------------------------------------------------*/
    /**
     * The class calculates and stores COG (course over ground) values 
     * and calculates the average value of them.
     */
    
    public class COG {

        private ArrayList<Double> listOfCOGValues;
        private int arraySize = 5;
        private Float lastLatitude = null;
        private Float lastLongitude = null;
        private double averageCOG = 0;
     
        /**
         * 
         */
        public COG() {
            this.listOfCOGValues = new ArrayList<>();
        }

        /**
        * Sets parameters for the calculation of the SOG
        * 
        * @param endLat current latitude of a vessel
        * @param endLon current longitude of a vessel
        * @param speed the speed of the vessel
        */
        public void setParameters(float endLat, float endLon, double speed) {
            double tempCOG;

            if (this.lastLatitude == null) 
            {
                this.averageCOG = 0;
                this.lastLatitude = endLat;
                this.lastLongitude = endLon;       
            } 
            
            else if (speed == 0)
            {
            this.averageCOG = 0;
            this.listOfCOGValues.clear();
            }
            
            
            // if distance travelled equals to zero and speed deos not equal to zero, do not proceed with the calculations,
            // as it is important not to add a zero value, received only because the distance travelled by the vessel is too 
            // short to be of influeance, into the array of values, because it greatly influences the averrage 
            //of cogs
            else if (!((this.lastLatitude - endLat) == 0 && (this.lastLongitude - endLon) == 0 && speed != 0))         
            {      
                tempCOG = getCOG(this.lastLatitude - endLat, this.lastLongitude - endLon);
                this.listOfCOGValues.add(tempCOG);
                //if the array list has no vectorSize quantity of values add one and calculate
                //the average
                if (this.listOfCOGValues.size() < arraySize) {
                    this.listOfCOGValues.add(tempCOG);
                    claculateTheAverageCOG();
                } else 
                //else remove the first one and add a new one at the end and
                //calculate the average
                {
                    this.listOfCOGValues.remove(0);
                    this.listOfCOGValues.add(tempCOG);
                    claculateTheAverageCOG();
                }
            }
        }

        /**
         * Computes and gets COG of a vessel.
         * 
         * @param changeInLat change in latitude
         * @param changeInLon change in longitude
         * @return average of the COG a vessel is travelling
         */
        
        public double getCOG(float changeInLat, float changeInLon) {
            double course;
            course = Math.toDegrees(Math.atan2(changeInLat, changeInLon));
            if (course < 0) {
                course = 360 + course;
            }
            return course;
        }

        /**
         * Calculates the average of COG values.
         */
        public void claculateTheAverageCOG() {
            double sum = 0;

            for (Double a : this.listOfCOGValues) {
                sum += a;
            }
            this.averageCOG = round((sum / this.listOfCOGValues.size()), 1);
        }

        /**
         * Gets COG of this object.
         * 
         * @return COG 
         */
        public double getCOG() {
            return round(this.averageCOG, 1);
        }
    }

/*-----COG:ending----------------------------------------------------------------------------------------------------------------*/
 
/*-----SOG:beginning----------------------------------------------------------------------------------------------------------------*/
    /**
     * The purpose of this class is to calculate and save SOG (speed over
     * ground) values. The DyenaBox instance uses this class to calculate 
     * the average SOG over predefined amount of seconds.
     *
     * @author Vilius Plepys
     * @author Amine Hajier - geodesic distance formulae and its deviations.
     */
    public class SOG {

        private ArrayList<Double> listOfSOGValues;
        private Double averageSOG = null;
        private Float lastLatitude = null;
        private Float lastLongitude = null;
        private int arraySize = 10;

        public SOG() {
            this.listOfSOGValues = new ArrayList<>();
        }

        /**
         * Sets the current location of the vessel
         * 
         * @param endLat current latitude 
         * @param endLong current longitude
         */
        public void setLocation(float endLat, float endLong) {

            double distance;

             if (this.lastLatitude != null) {
                distance = getDistance(this.lastLatitude, this.lastLongitude, endLat, endLong);
            } else {
                distance = 0;
            }

            this.lastLatitude = endLat;
            this.lastLongitude = endLong;

            double tempSog;
            tempSog = (distance / (1 * 0.000277778));

            //if the array has no vectorSize quantity of values, add one and calculate
            //the average
            if (this.listOfSOGValues.size() < arraySize) {
                this.listOfSOGValues.add(tempSog);
                claculateTheAverageSOG();
            } else 
            //else remove the first one and add new one at the end and
            //calculate the average
            {
                this.listOfSOGValues.remove(0);
                this.listOfSOGValues.add(tempSog);
                claculateTheAverageSOG();
            }
        }

       /**
        * Computes an average from SOG values
        */
        public void claculateTheAverageSOG() {
            double sum = 0;

            //avoiding division by 0 in the conditional branch
            if (this.listOfSOGValues.size() == 0)
            {
                this.averageSOG = 0.0;
            }
            
                 else
                
            {
                 for (Double a : this.listOfSOGValues) {
                    sum += a;
                }    
                 this.averageSOG = round((sum / this.listOfSOGValues.size()), 1);
            }
        }

        /**
         * Returns the average speed over ground.
         *
         * @return sog
         */
        public double getSOG() {
            return this.averageSOG;            
        }

        /**
         * Get the distance between two points on earth using latitude and
         * longitude in nautical miles.
         * (Code from Ian Week's WMMS Parser program)
         *
         * @param startLat The starting latitude.
         * @param startLong The starting longitude.
         * @param endLat The ending latitude.
         * @param endLong The ending longitude.
         * @return The distance between the two points.
         */
        private double getDistance(float startLat, float startLong, float endLat, float endLong) {

            //dist = arccos(sin(lat1) · sin(lat2) + cos(lat1) · cos(lat2) · cos(lon1 - lon2)) · R
            float latDistance = (float) Math.toRadians(startLat - endLat);
            float lngDistance = (float) Math.toRadians(startLong - endLong);

            float a = (float) (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                    * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2));

            float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

            //Distance in nautical miles.
            final float AVERAGE_RADIUS_OF_EARTH = 6371;
            return round((AVERAGE_RADIUS_OF_EARTH * c * 0.539956803), 3);
        }

     /**
         * Rounds a number to n decimal places
         * @param value the number
         * @param places number of decimal places
         * @return rounded number
         */
        public double round(double value, int places) {
            return Math.round(value * Math.pow(10d, places)) / Math.pow(10d, places);
        }
    }

    /*-----SOG:ending----------------------------------------------------------------------------------------------------------------*/
       
    
    
    
       /**
         * Rounds a number to n decimal places
         * @param value the number
         * @param places number of decimal places
         * @return rounded number
         */
    public double round(double value, int places) {
        return Math.round(value * Math.pow(10d, places)) / Math.pow(10d, places);
    }

}