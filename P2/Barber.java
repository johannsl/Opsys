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
			Customer firstCustomer = queue.removeCustomer();
			if (firstCustomer != null)
			{
				gui.barberIsAwake(pos);
				gui.fillBarberChair(pos, firstCustomer);
				try
				{
					Thread.sleep(MIN_BARBER_WORK+(int)(Math.random()*(MAX_BARBER_WORK-MIN_BARBER_WORK+1)));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gui.emptyBarberChair(pos);
				try
				{
					gui.barberIsSleeping(pos);
					Thread.sleep(MIN_BARBER_SLEEP+(int)(Math.random()*(MAX_BARBER_SLEEP-MIN_BARBER_SLEEP+1)));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	// Add more methods as needed
}

