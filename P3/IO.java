import java.util.Random;

/**
 * Created by iver on 08.04.15.
 */
public class IO {
	private EventQueue eventQueue;
    private Queue ioQueue;
    private Gui gui;
    private long clock;
    private Statistics statistics;
    private long avgIOTime;
    private Process activeProcess;

    public IO(EventQueue eventQueue, Queue ioQueue, Gui gui, long clock, Statistics statistics, long avgIOTime) {
    	this.eventQueue = eventQueue;
    	this.ioQueue = ioQueue;
        this.gui = gui;
        this.clock = clock;
        this.statistics = statistics;
        this.avgIOTime = avgIOTime;
    }

    public void insertProcess(Process process) {
        if (process != null)
        {
        	ioQueue.insert(process);
        	process.enterIOQueue(clock);
        }
        
        // TEST PRINT
//        else System.out.print("IO insert process = null! \n");
        
    }
    
    public Process extractProcess() {
    	if (activeProcess != null) {
    		Process process = activeProcess;
    		activeProcess = null;
    		process.leaveIO(clock);
    		gui.setIoActive(null);
    		return process;
    	}
    	
    	// TEST PRINT
//    	System.out.print("There was no IO process to extract! \n");
    	
    	return null;
    }
    
    public boolean isIdle() {
    	return (activeProcess == null);
    }

    public void run() {
    	if (!ioQueue.isEmpty()) {
    		if (isIdle()) {
    			activeProcess = (Process) ioQueue.removeNext();
    			activeProcess.enterIO(clock);
    			gui.setIoActive(activeProcess);
    			long ioTime = activeProcess.calcIoOperationTime(avgIOTime);
    			eventQueue.insertEvent(new Event(Constants.END_IO, clock + ioTime));
    		}
    	}
    	
    	// TEST PRINT
//    	else System.out.print("The ioQueue was empty! \n");
    }
    
	public void updateClock(long clock) {
		//TEST PRINT
//		System.out.print("The IO clock is now: " + clock + "\n");
		
		this.clock = clock;
	}  
    
	public void timePassed(long timePassed) {
		statistics.ioQueueLengthTime += ioQueue.getQueueLength()*timePassed;
		if (ioQueue.getQueueLength() > statistics.ioQueueLargestLength) {
			statistics.ioQueueLargestLength = ioQueue.getQueueLength();
		}
	}
}
