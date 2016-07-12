
package Classes;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * This class instantiates <code>DyenaBox</code> objects, every second activates them
 * and gets <code>Message</code> instances from them.
 * 
 * @author Vilius Plepys
 */
public class DyenaBoxesHandler implements Runnable
{
     
    private DyenaBox[] boxes;

    private volatile ArrayList<Object> messagesToDisplay;

    
    public DyenaBoxesHandler()
    {
        this.boxes = new DyenaBox[10]; // 10 is the maximum number of Dyena boxes to be simulated
        
        //adding empty DyenaBox objects, so to add data files later
        for (int i=0; i<10; i++)
        {
          boxes[i] = new DyenaBox(i);          
        }
      
    }
     
    /**
     * Gets a <code>DyenaBox</code> instance.
     * 
     * @param i index of a <code>DyenaBox</code> instance in an array
     * @return <code>DyenaBox</code> instance
     */
    public DyenaBox getBox(int i)
    {
        return this.boxes[i];
    }

    /**
     * Gets <code>Message</code> instances from all <code>DyenaBox</code> instances.
     * (All messages from Dyena box entities.)
     * 
     * @return  array list of <code>Message</code> instances
     */
    public ArrayList<Object> getMessagesToDisplay()
    {
        return this.messagesToDisplay;
    }

    
    /**
     * Runs a thread that activates <code>DyenaBox</code> instances that are in 
     * state of simulation. Gets <code>Message</code> instances from them, unless
     * they are in a blackout state.
     */
    @Override
    public void run()
    {
        ArrayList<Thread> threadList = new ArrayList<>();
             
        while (true)
        {
            try
            {
                for (DyenaBox box : boxes)
                {
                  if (box.isSimulated()) threadList.add(new Thread(box));    
                }

                for (Thread thread : threadList)
                {
                    thread.start();
                }
                
                for (Thread thread : threadList)
                {
                    thread.join();
                }
                
                this.messagesToDisplay = new ArrayList<Object>();
                
                for (int i = 0; i < boxes.length; i++)
                {                  
                    if (boxes[i].isSimulated() && !boxes[i].isBlackout()) //:add a message only if a box is in simulation
                    {
                    this.messagesToDisplay.add(boxes[i].getMessage());
                    }
                    if (boxes[i].isBlackout())
                    {
                    this.messagesToDisplay.add(null);
                    }
                }
                
                threadList.clear();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
