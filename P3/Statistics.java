/**
 * This class contains a lot of public variables that can be updated
 * by other classes during a simulation, to collect information about
 * the run.
 */
public class Statistics {
	/** The number of processes that have exited the system */
	public long nofCompletedProcesses = 0;
	/** The number of processes that have entered the system */
	public long nofCreatedProcesses = 0;
	/** The total time that all completed processes have spent waiting for memory */
	public long totalTimeSpentWaitingForMemory = 0;
	/** The time-weighted length of the memory queue, divide this number by the total time to get average queue length */
	public long memoryQueueLengthTime = 0;
	/** The largest memory queue length that has occured */
	public long memoryQueueLargestLength = 0;
	
	public long finalTime = 0;
	public long processSwitches = 0;
	public long ioOperations = 0;
	public double averageThroughput = 0;
	
	public long cpuTimeProcessing = 0;
	public double fractionCpuProcessing = 0;
	public long cpuTimeWaiting = 0;
	public double fractionCpuWaiting = 0;
	
	public long cpuQueueLargestLength = 0;
	public long cpuQueueLengthTime = 0;
	public long ioQueueLargestLength = 0;
	public long ioQueueLengthTime = 0;
	
	public double memoryQueueAverageTimes = 1;
	public long cpuQueueTotalTimes = 0;
	public double cpuQueueAverageTimes = 0;
	public long ioQueueTotalTimes = 0;
	public double ioQueueAverageTimes = 0;
	
	public double averageTimeInSystem = 0;
	public long totalTimeSpentInSystem = 0;
	public double averageTimeSpentWaitingForMemory = 0;
	public double averageTimeSpentWaitingForCpu = 0;
	public long totalTimeSpentWaitingForCpu = 0;
	public double averageTimeSpentProcessing = 0;
	public long totalTimeSpentWaitingForIo = 0;
	public long totalTimeSpentInIo = 0;
    
	/**
	 * Prints out a report summarizing all collected data about the simulation.
	 * @param simulationLength	The number of milliseconds that the simulation covered.
	 */
	public void printReport(long simulationLength) {
		averageThroughput = ((double)nofCompletedProcesses/finalTime)*1000;
		fractionCpuProcessing = ((double)cpuTimeProcessing/finalTime*100);
		fractionCpuWaiting = ((double)(finalTime-cpuTimeProcessing)/finalTime*100);
		cpuQueueAverageTimes = ((double)cpuQueueTotalTimes/nofCompletedProcesses);
		ioQueueAverageTimes = ((double)ioQueueTotalTimes/nofCompletedProcesses);
		
		// TEST PRINT
//		System.out.print("CPU time processing: "+cpuTimeProcessing+", CPU queue total: "+cpuQueueTotalTimes+", IO queue total: "+ioQueueTotalTimes);
		
		System.out.println();
		System.out.println("Simulation statistics:");
		System.out.println();
		System.out.println("Final time                                                    "+finalTime+" ms");	
		System.out.println("Number of completed processes:                                "+nofCompletedProcesses);
		System.out.println("Number of created processes:                                  "+nofCreatedProcesses);
		System.out.println("Number of (forced) process switches:                          "+processSwitches);
		System.out.println("Number of processed I/O operations:                           "+ioOperations);
		System.out.println("Average throughput (process per second):                      "+averageThroughput);
		System.out.println();
		System.out.println("Total CPU time spent processing:                              "+cpuTimeProcessing+" ms");
		System.out.println("Fraction of CPU time spent processing:                        "+fractionCpuProcessing+" %");
		System.out.println("Total CPU time spent waiting:                                 "+(finalTime-cpuTimeProcessing)+" ms");
		System.out.println("Fraction of CPU time spent waiting:                           "+fractionCpuWaiting+" %");
		System.out.println();
		System.out.println("Largest occuring memory queue length:                         "+memoryQueueLargestLength);
		System.out.println("Average memory queue length:                                  "+(float)memoryQueueLengthTime/simulationLength);
		System.out.println("Largest occuring CPU queue length:                            "+cpuQueueLargestLength);
		System.out.println("Average CPU queue length:                                     "+(float)cpuQueueLengthTime/simulationLength);
		System.out.println("Largest occuring I/O queue length:                            "+ioQueueLargestLength);
		System.out.println("Average I/O queue length:                                     "+(float)ioQueueLengthTime/simulationLength);
		System.out.println("Average # of times a process has been placed in memory queue: "+memoryQueueAverageTimes);
		System.out.println("Average # of times a process has been placed in cpu queue:    "+cpuQueueAverageTimes);
		System.out.println("Average # of times a process has been placed in I/O queue:    "+ioQueueAverageTimes);
		System.out.println();
		
		if (nofCompletedProcesses > 0) {
			System.out.println("Average time spent in system per process:                     "+
					totalTimeSpentInSystem/nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for memory per process:            "+ 
					totalTimeSpentWaitingForMemory/nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for cpu per process:               "+
					totalTimeSpentWaitingForCpu/nofCompletedProcesses+" ms");
			System.out.println("Average time spent processing per process:                    "+
					cpuTimeProcessing/nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for I/O per process:               "+
				totalTimeSpentWaitingForIo/nofCompletedProcesses+" ms");
			System.out.println("Average time spent in I/O per process:                        "+
					totalTimeSpentInIo/nofCompletedProcesses+" ms");
		}
		else {
			System.out.println("Average time spent in system per process:                     inf ms");
			System.out.println("Average time spent waiting for memory per process:            inf ms");
			System.out.println("Average time spent waiting for cpu per process:               inf ms");
			System.out.println("Average time spent processing per process:                    inf ms");
			System.out.println("Average time spent waiting for I/O per process:               inf ms");
			System.out.println("Average time spent in I/O per process:                        inf ms");
		}
	}
}
