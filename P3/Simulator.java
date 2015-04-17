import java.io.*;

/**
 * The main class of the P3 exercise. This class is only partially complete.
 */
public class Simulator implements Constants {
	/** The queue of events to come */
    private EventQueue eventQueue;
	/** Reference to the memory unit */
    private Memory memory;
	/** Reference to the GUI interface */
	private Gui gui;
	/** Reference to the statistics collector */
	private Statistics statistics;
	/** The global clock */
    private long clock;
	/** The length of the simulation */
	private long simulationLength;
	/** The average length between process arrivals */
	private long avgArrivalInterval;
	// Add member variables as needed

	private CPU CPU;
	private IO IO;

	/**
	 * Constructs a scheduling simulator with the given parameters.
	 * @param memoryQueue			The memory queue to be used.
	 * @param cpuQueue				The CPU queue to be used.
	 * @param ioQueue				The I/O queue to be used.
	 * @param memorySize			The size of the memory.
	 * @param maxCpuTime			The maximum time quant used by the RR algorithm.
	 * @param avgIoTime				The average length of an I/O operation.
	 * @param simulationLength		The length of the simulation.
	 * @param avgArrivalInterval	The average time between process arrivals.
	 * @param gui					Reference to the GUI interface.
	 */
	public Simulator(Queue memoryQueue, Queue cpuQueue, Queue ioQueue, long memorySize,
			long maxCpuTime, long avgIoTime, long simulationLength, long avgArrivalInterval, Gui gui) {
		this.simulationLength = simulationLength;
		this.avgArrivalInterval = avgArrivalInterval;
		this.gui = gui;
		statistics = new Statistics();
		eventQueue = new EventQueue();
		memory = new Memory(memoryQueue, memorySize, statistics);
		clock = 0;
		// Add code as needed
		CPU = new CPU(eventQueue, cpuQueue, gui, clock, statistics, maxCpuTime);
		IO = new IO(eventQueue, ioQueue, gui, clock, statistics, avgIoTime);
    }

    /**
	 * Starts the simulation. Contains the main loop, processing events.
	 * This method is called when the "Start simulation" button in the
	 * GUI is clicked.
	 */
	public void simulate() {
		// TODO: You may want to extend this method somewhat.

		System.out.print("Simulating... \n");
		// Genererate the first process arrival event
		eventQueue.insertEvent(new Event(NEW_PROCESS, 0));
		// Process events until the simulation length is exceeded:
		while (clock < simulationLength && !eventQueue.isEmpty()) {
			// Find the next event
			Event event = eventQueue.getNextEvent();
			// Find out how much time that passed...
			long timeDifference = event.getTime()-clock;
			
			// TEST PRINT
//			System.out.print("\n" + timeDifference + " has passed since last event \n");
			
			// ...and update the clock.	
			clock = event.getTime();
			CPU.updateClock(clock);
			IO.updateClock(clock);
			// Let the memory unit and the GUI know that time has passed
			memory.timePassed(timeDifference);
			gui.timePassed(timeDifference);
			CPU.timePassed(timeDifference);
			IO.timePassed(timeDifference);
			// Deal with the event
			if (clock < simulationLength) {
				processEvent(event);
			}
			// Note that the processing of most events should lead to new
			// events being added to the event queue!
		}
		
		// TEST PRINT
		if (clock >= simulationLength) {
			System.out.println("Time has run out! \n");
		}
		
		System.out.println("..done.");
		// End the simulation by printing out the required statistics
		statistics.finalTime = clock;
		statistics.printReport(simulationLength);
	}

	/**
	 * Processes an event by inspecting its type and delegating
	 * the work to the appropriate method.
	 * @param event	The event to be processed.
	 */
	private void processEvent(Event event) {
		switch (event.getType()) {
			case NEW_PROCESS:
				createProcess();
				break;
			case SWITCH_PROCESS:
				switchProcess();
				break;
			case END_PROCESS:
				endProcess();
				break;
			case IO_REQUEST:
				processIoRequest();
				break;
			case END_IO:
				endIoOperation();
				break;
		}
	}

	/**
	 * Simulates a process arrival/creation.
	 */
	private void createProcess() {
		// Create a new process
		Process newProcess = new Process(memory.getMemorySize(), clock);
		
		//TEST PRINT
//		System.out.print(newProcess.toString() + ": CPU TIME: " + newProcess.getCpuTimeNeeded() + ", MEMORY NEED: " + newProcess.getMemoryNeeded() + "\n");
		
		memory.insertProcess(newProcess);
		flushMemoryQueue();
		// Add an event for the next process arrival
		long nextArrivalTime = clock + 1 + (long)(2*Math.random()*avgArrivalInterval);
		eventQueue.insertEvent(new Event(NEW_PROCESS, nextArrivalTime));
		// Update statistics
		statistics.nofCreatedProcesses++;
    }

	/**
	 * Transfers processes from the memory queue to the ready queue as long as there is enough
	 * memory for the processes.
	 */
	private void flushMemoryQueue() {
		Process p = memory.checkMemory(clock);
		// As long as there is enough memory, processes are moved from the memory queue to the cpu queue
		while(p != null) {
			// TODO: Add this process to the CPU queue!
			// Also add new events to the event queue if needed
			
			//TEST PRINT
//			System.out.print("Inserting " + p.toString() + " to the CPU queue \n");
			
			CPU.insertProcess(p);
			CPU.run();
			// Since we haven't implemented the CPU and I/O device yet,
			// we let the process leave the system immediately, for now.
			// memory.processCompleted(p);
			// Try to use the freed memory:
//			flushMemoryQueue();
			// Update statistics
			//p.updateStatistics(statistics);
			// Check for more free memory
			p = memory.checkMemory(clock);
			
			// TEST PRINT
			System.out.print("Free memory: " + memory.getFreeMemory() + "\n");
			
		}
	}

	/**
	 * Simulates a process switch.
	 */
	private void switchProcess() {
		Process p = CPU.extractProcess();
		CPU.insertProcess(p);
		CPU.run();
		statistics.processSwitches++;
	}

	/**
	 * Ends the active process, and deallocates any resources allocated to it.
	 */
	private void endProcess() {
		Process p = CPU.extractProcess();
		memory.processCompleted(p);
		CPU.run();
		flushMemoryQueue();
		p.updateStatistics(statistics);

		// TEST PRINT
		System.out.print("Free memory: " + memory.getFreeMemory() + "\n");
		
	}

	/** 
	 * Processes an event signifying that the active process needs to
	 * perform an I/O operation.
	 */
	private void processIoRequest() {
		Process p = CPU.extractProcess();
		IO.insertProcess(p);
		CPU.run();
		IO.run();
	}

	/**
	 * Processes an event signifying that the process currently doing I/O
	 * is done with its I/O operation.
	 */
	private void endIoOperation() {
		Process p = IO.extractProcess();
		CPU.insertProcess(p);
		IO.run();
		CPU.run();
		statistics.ioOperations++;
	}

	/**
	 * Reads a number from the an input reader.
	 * @param reader	The input reader from which to read a number.
	 * @return			The number that was inputted.
	 */
	public static long readLong(BufferedReader reader) {
		try {
			return Long.parseLong(reader.readLine());
		} catch (IOException ioe) {
			return 100;
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	/**
	 * The startup method. Reads relevant parameters from the standard input,
	 * and starts up the GUI. The GUI will then start the simulation when
	 * the user clicks the "Start simulation" button.
	 * @param args	Parameters from the command line, they are ignored.
	 */
	public static void main(String args[]) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please input system parameters: ");
		System.out.print("Memory size (KB): ");
		long memorySize = readLong(reader);
//		long memorySize = 2048L;
		while(memorySize < 400) {
			System.out.println("Memory size must be at least 400 KB. Specify memory size (KB): ");
			memorySize = readLong(reader);
		}

		System.out.print("Maximum uninterrupted cpu time for a process (ms): ");
		long maxCpuTime = readLong(reader);
//		long maxCpuTime = 500L;

		System.out.print("Average I/O operation time (ms): ");
		long avgIoTime = readLong(reader);
//		long avgIoTime = 225L;

		System.out.print("Simulation length (ms): ");
		long simulationLength = readLong(reader);
//		long simulationLength = 250000L;
		while(simulationLength < 1) {
			System.out.println("Simulation length must be at least 1 ms. Specify simulation length (ms): ");
			simulationLength = readLong(reader);
		}

		System.out.print("Average time between process arrivals (ms): ");
		long avgArrivalInterval = readLong(reader);
//		long avgArrivalInterval = 5000L;

		@SuppressWarnings("unused")
		SimulationGui gui = new SimulationGui(memorySize, maxCpuTime, avgIoTime, simulationLength, avgArrivalInterval);
	}
}
