/**
 * Created by iver on 08.04.15.
 */
public class CPU {
    private Queue mQueue;
    private Process mProcess;

    public CPU(Queue queue) {
        this.mQueue = queue;
    }

    public void insertProcess(Process process) {
        mQueue.insert(process);
        if (mProcess == null) mProcess = (Process) mQueue.removeNext();
    }

    public Process popProcess() {
        if (mQueue.isEmpty()) return mProcess;
        Process process = mProcess;
        mProcess = (Process) mQueue.removeNext();
        return process;
    }

}
