import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * This class implements a queue of customers as a circular buffer.
 */
@SuppressWarnings("unused")
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
    
    public synchronized void addCustomer(Customer lastCustomer, Doorman doorman)
    {
    	while (queue.size() >= queueLength) 
    	{
    		gui.println("Queue is full!");
    		try
        	{
                gui.println("Doorman is waiting...");
    			wait();
        	}
        	catch(InterruptedException e)
        	{
                e.printStackTrace();
        	}
    	}
    	gui.fillLoungeChair(firstAvailableSeat, lastCustomer);
		gui.println("Customer: " + lastCustomer.getCustomerID() + " sits in seat: " + firstAvailableSeat);
		queue.add(lastCustomer);
		if (firstAvailableSeat < 17) firstAvailableSeat++;
		else firstAvailableSeat = 0;
		if (queue.size() == 1) 
		{
			gui.println("Barbers are notified.");
			notifyAll();
		}
    }
    
    public synchronized Customer removeCustomer(Barber barber, int pos)
    {
    	while (queue.size() <= 0)
    	{
    		gui.println("Queue is empty!");
    		try
    		{
    			gui.println("Barber: " + pos + " is waiting...");
    			wait();
    		}
    		catch(InterruptedException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	Customer firstCustomer = queue.remove(0);
		gui.emptyLoungeChair(firstInLine);
		if (firstInLine < 17) firstInLine++;
		else firstInLine = 0;
		if (queue.size() == queueLength-1)
		{
			gui.println("Doorman is notified.");
			notifyAll();
		}
		return firstCustomer;
    }
}
