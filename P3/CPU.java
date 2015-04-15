/**
 * Created by iver on 08.04.15.
 */
public class CPU {
	private EventQueue eventQueue;
    private Queue cpuQueue;
    private Gui gui;
    private long clock;
    private Statistics statistics;
    private long maxCPUTime;
    private Process activeProcess;

	public CPU(EventQueue eventQueue, Queue cpuQueue, Gui gui, long clock, Statistics statistics, long maxCPUTime) {
		this.eventQueue = eventQueue;
        this.cpuQueue = cpuQueue;
        this.gui = gui;
        this.clock = clock;
        this.statistics = statistics;
        this.maxCPUTime = maxCPUTime;
    }
	
    public void insertProcess(Process process) {
        cpuQueue.insert(process);
        process.enterCPUQueue(clock);
    }
    
    public Process extractProcess() {
    	Process process = activeProcess;
    	activeProcess = null;
    	process.leaveCPU(clock);
    	return process;
    }
    
    public boolean isIdle() {
    	return (activeProcess == null);
    }
    
    public void run() {
    	Process activeProcess = (Process) cpuQueue.removeNext();
    	activeProcess.enterCPU(clock);
    	gui.setCpuActive(activeProcess);
    	long nextIo = activeProcess.calcTimeToNextIoOperation();
    	if (activeProcess.getCpuTimeNeeded() > maxCPUTime && nextIo > maxCPUTime) {
    		eventQueue.insertEvent(new Event(Constants.SWITCH_PROCESS, clock + maxCPUTime));
    	}
    	else if (nextIo < maxCPUTime && nextIo < activeProcess.getCpuTimeNeeded()) {
    		eventQueue.insertEvent(new Event(Constants.IO_REQUEST, clock + nextIo));
    	}
    	else if (activeProcess.getCpuTimeNeeded() <= maxCPUTime && activeProcess.getCpuTimeNeeded() <= nextIo) {
    		eventQueue.insertEvent(new Event(Constants.END_PROCESS, clock + activeProcess.getCpuTimeNeeded()));
    	}
    	else {System.out.print("PLEASE CHECK CPU FOR BUGS!");}
    }
    
    
	public void updateClock(long clock) {
		//TEST PRINT
		System.out.print("The CPU clock is now: " + clock + "\n");
		
		this.clock = clock;
	}
	
	public void timePassed(long timePassed) {
		statistics.cpuQueueLengthTime += cpuQueue.getQueueLength()*timePassed;
		if (cpuQueue.getQueueLength() > statistics.cpuQueueLargestLength) {
			statistics.cpuQueueLargestLength = cpuQueue.getQueueLength();
		}
	}
	
    public long getMaxCPUTime() {
		return maxCPUTime;
	}

	public void setMaxCPUTime(long maxCPUTime) {
		this.maxCPUTime = maxCPUTime;
	}


    
    
 

    
//    public Process getActive() {
//    	Process process = mProcess;
//    	mProcess = null;
//    	return process;
//    }
//
//    public Process popProcess() {
//        if (mQueue.isEmpty()) return mProcess;
//        Process process = mProcess;
//        mProcess = (Process) mQueue.removeNext();
//        return process;
//    }
//
//    public Process start() {
//    	if (mQueue.isEmpty()) {
//    		mProcess = null;
//    		return mProcess;
//    	} else {
//    		mProcess = (Process) mQueue.removeNext();
//    		return mProcess;
//    	}
//    }
    
}
