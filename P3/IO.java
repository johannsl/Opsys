import java.util.Random;

/**
 * Created by iver on 08.04.15.
 */
public class IO {
    private Queue mQueue;
    private long clock;
    private Gui gui;
    private Statistics statistics;
    private long avgIOTime;
    private Random mRandom;
    private Process mProcess;

    public IO(Queue queue, long clock, Gui gui, Statistics statistics, long avgIOTime) {
    	this.mQueue = queue;
        this.clock = clock;
        this.gui = gui;
        this.statistics = statistics;
        this.avgIOTime = avgIOTime;
        mRandom = new Random();
    }

    public boolean insertProcess(Process process) {
        mQueue.insert(process);
        if (mProcess == null) {
        	if (mProcess == null) mProcess = (Process) mQueue.removeNext();
        	return true;
        }
        return false;
    }

	public void timePassed(long timePassed) {
		statistics.ioQueueLengthTime += mQueue.getQueueLength()*timePassed;
		if (mQueue.getQueueLength() > statistics.ioQueueLargestLength) {
			statistics.ioQueueLargestLength = mQueue.getQueueLength();
		}
	}
    
	public void updateClock(long clock) {
		//TEST PRINT
		System.out.print("The IO clock is now: " + clock + "\n");
		
		this.clock = clock;
	}
    
	
	
	
    public long getIOTime() {
    	return (long) (Math.pow(mRandom.nextDouble(), 2) * 2 * avgIOTime);
    }
    
    public Process getProcess() {
    	Process process = mProcess;
    	mProcess = null;
    	return process;
    }
    
    public Process popProcess() {
        return (Process) mQueue.removeNext();
    }
    
    public Process begin() {
    	if (mQueue.isEmpty()) return null;
    	mProcess = (Process) mQueue.removeNext();
    	return mProcess;
    }
}
