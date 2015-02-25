/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 */
public class Doorman implements Runnable, Constants
{	
	CustomerQueue queue;
	Gui gui;
	Thread doorman;
	boolean flag;
	
	/**
	 * Creates a new doorman.
	 * @param queue		The customer queue.
	 * @param gui		A reference to the GUI interface.
	 */
	public Doorman(CustomerQueue queue, Gui gui) 
	{ 
		this.queue = queue;
		this.gui = gui;
		doorman = new Thread(this, "Doorman");
		flag = true;
	}

	/**
	 * Starts the doorman running as a separate thread.
	 */
	public void startThread() 
	{
		doorman.start();
	}

	/**
	 * Stops the doorman thread.
	 */
	public void stopThread() 
	{
		flag = false;
	}
	
	@Override
	public void run()
	{
		while(flag)
		{
			Customer lastCustomer = new Customer();
			queue.addCustomer(lastCustomer);
			try
			{
				Thread.sleep(MIN_DOORMAN_SLEEP+(int)(Math.random()*(MAX_DOORMAN_SLEEP-MIN_DOORMAN_SLEEP+1)));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	// Add more methods as needed
}
