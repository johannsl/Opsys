import java.util.Random;

/**
 * Created by iver on 08.04.15.
 */
public class IO {
    private Queue mQueue;
    private Process mProcess;
    private Random mRandom;
    private long avgIOTime;

    public IO(Queue queue, long avgIOTime) {
    	mRandom = new Random();
        this.mQueue = queue;
        this.avgIOTime = avgIOTime;
    }

    public boolean insertProcess(Process process) {
        mQueue.insert(process);
        if (mProcess == null) {
        	if (mProcess == null) mProcess = (Process) mQueue.removeNext();
        	return true;
        }
        return false;
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
