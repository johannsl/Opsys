package threadTest;

public class Skjerm
{
	// Siden denne funksjonen er synchronized,
	// kan bare en tråd være inne i den av gangen
	public synchronized void skrivUt(String ord1, String ord2) 
	{
		System.out.print(ord1);
		try 
		{
			Thread.sleep(100);
		} 
		catch (InterruptedException e) {}
		System.out.println(ord2);
	 }
}
