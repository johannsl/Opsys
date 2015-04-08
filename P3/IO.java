/**
 * Created by iver on 08.04.15.
 */
public class IO {
    private Queue mQueue;
    private Process mProcess;

    public IO(Queue queue) {
        this.mQueue = queue;
    }

    public void insertProcess(Process process) {
        mQueue.insert(process);
        if (mProcess == null) mProcess = (Process) mQueue.removeNext();
    }

    public Process popProcess() {
        return (Process) mQueue.removeNext();
    }
}
