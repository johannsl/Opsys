import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue 
{	
	Gui gui;
	int queueLength;
	List<Customer> queue;
	int firstInLine;
	int firstAvailableSeat;
	
	/**
	 * Creates a new customer queue.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */
    public CustomerQueue(int queueLength, Gui gui) 
    {
		this.queueLength = queueLength;
		this.gui = gui;
		queue = new ArrayList<Customer>();
		firstInLine = 0;
		firstAvailableSeat = 0;
	}
    
    public void addCustomer(Customer lastCustomer, Doorman doorman)
    {
    	if (queue.size() < queueLength) 
    	{ 
    		gui.fillLoungeChair(firstAvailableSeat, lastCustomer);
    		gui.println("Customer: " + lastCustomer.getCustomerID() + " sits in seat: " + firstAvailableSeat);
    		queue.add(lastCustomer);
    		if (firstAvailableSeat < 17) firstAvailableSeat++;
    		else firstAvailableSeat = 0;
    	}
    	else gui.println("Queue is full!");
    	try
    	{
            doorman.wait();
            gui.println("Doorman is waiting...");
        }
    	catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public synchronized Customer removeCustomer(Barber barber, int pos)
    {
    	if (queue.size() > 0)
    	{
    		Customer firstCustomer = queue.remove(0);
    		gui.emptyLoungeChair(firstInLine);
    		if (firstInLine < 17) firstInLine++;
    		else firstInLine = 0;
    		return firstCustomer;
    	}
    	try
    	{
    		barber.wait();
    		gui.println("Barber: " + pos + " is waiting...");
        }
    	catch(InterruptedException e)
    	{
    		e.printStackTrace();
        }
    	// This caused mass messaging if there barber didn't have an additional sleep at the end of run.
    	
    	// else gui.println("Queue is empty!");
    	return null;
    }
    
	// Add more methods as needed
}
