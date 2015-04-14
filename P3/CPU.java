/**
 * Created by iver on 08.04.15.
 */
public class CPU {
    private Queue mQueue;
    private Process mProcess;
    private long maxCPUTime;

	public CPU(Queue queue, long maxCPUTime) {
        this.mQueue = queue;
        this.maxCPUTime = maxCPUTime;
    }
	
    public long getMaxCPUTime() {
		return maxCPUTime;
	}

	public void setMaxCPUTime(long maxCPUTime) {
		this.maxCPUTime = maxCPUTime;
	}

    public boolean insertProcess(Process process, long clock) {
        mQueue.insert(process);
        process.enterCPUQueue(clock);
        if (mProcess == null) {
        	mProcess = (Process) mQueue.removeNext();
        	mProcess.enterCPU(clock);
        	return true;
        }
        return false;
    }
    
    
    
    public boolean isIdle() {
    	return mProcess == null;
    }
    
    public Process getActive() {
    	Process process = mProcess;
    	mProcess = null;
    	return process;
    }


    public Process popProcess() {
        if (mQueue.isEmpty()) return mProcess;
        Process process = mProcess;
        mProcess = (Process) mQueue.removeNext();
        return process;
    }

    public Process start() {
    	if (mQueue.isEmpty()) {
    		mProcess = null;
    		return mProcess;
    	} else {
    		mProcess = (Process) mQueue.removeNext();
    		return mProcess;
    	}
    }
    
}
