/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 */
public class Barber implements Runnable, Constants
{
	CustomerQueue queue;
	Gui gui;
	int pos;
	Thread barber;
	boolean flag;
	
	/**
	 * Creates a new barber.
	 * @param queue		The customer queue.
	 * @param gui		The GUI.
	 * @param pos		The position of this barber's chair
	 */
	public Barber(CustomerQueue queue, Gui gui, int pos) 
	{ 
		this.queue = queue;
		this.gui = gui;
		this.pos = pos;
		barber = new Thread(this, "barber" + pos);
		flag = true;
	}

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() 
	{
		barber.start();
	}

	/**
	 * Stops the barber thread.
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
			Customer firstCustomer = queue.removeCustomer(this, pos);
			if (firstCustomer != null)
			{
				gui.barberIsAwake(pos);
				gui.fillBarberChair(pos, firstCustomer);
				gui.println("Customer: " + firstCustomer.getCustomerID() + " is served by barber: " + pos);
				try
				{
					Thread.sleep(MIN_BARBER_WORK+(int)(Math.random()*(Globals.barberWork-MIN_BARBER_WORK+1)));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gui.emptyBarberChair(pos);
				try
				{
					gui.barberIsSleeping(pos);
					Thread.sleep(MIN_BARBER_SLEEP+(int)(Math.random()*(Globals.barberSleep-MIN_BARBER_SLEEP+1)));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			// This additional sleep was implemented to hinder messages from flooding the TextArea. 
			// Those messages was commented out.
			
			//try
			//{
			//	Thread.sleep(3000);
			//}
			//catch (InterruptedException e)
			//{
			//	e.printStackTrace();
			//}
		}
	}
	// Add more methods as needed
}

