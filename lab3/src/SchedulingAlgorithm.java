// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.*;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector<Process> processVector, Results result, int quantum) {
    int comptime = 0;
    int size = processVector.size();
    int completed = 0;
    Map<Integer, Integer> blockedProcesses = new HashMap<>();

    String resultsFile = "Summary-Processes";

    result.schedulingType = "Interactive (Preemptive)";
    result.schedulingName = "Lottery";
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      Vector<Process> processes = (Vector<Process>) processVector.clone();
      long ticketsAmount = 0;
      for (int i = 0; i < size; i++) {
        ticketsAmount +=  processes.elementAt(i).ticketsAmount;
      }
      Lottery lottery = new Lottery(ticketsAmount);
      int currProcessIndex = lottery.run(processes);
      Process currProcess = processes.elementAt(currProcessIndex);

      out.println("Process: " + currProcess.processNumber + " registered... ("
              + currProcess.cputime + " " + currProcess.ioblocking + " " + currProcess.cpudone + ")");

      currProcess.cpudone++;
      comptime++;

      while (comptime < runtime) {
        if (currProcess.cpudone >= currProcess.cputime) {
          completed++;
          out.println("Process: " + currProcess.processNumber + " completed... ("
                  + currProcess.cputime + " " + currProcess.ioblocking + " " + currProcess.cpudone + ")");

          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }

          lottery.setTicketsAmount(lottery.getTicketsAmount() - currProcess.ticketsAmount);
          processes.remove(currProcess);
          blockedProcesses.remove(currProcess.processNumber);

          if (processes.size() == 1) {
            currProcess = processes.elementAt(0);
            currProcessIndex = 0;
          } else {
            currProcess.currQuantum = quantum;
          }
        }

        if (currProcess.ioblocking == currProcess.ionext && blockedProcesses.size() < processes.size() - 1) {
          out.println("Process: " + currProcess.processNumber + " I/O blocked... ("
                  + currProcess.cputime + " " + currProcess.ioblocking + " " + currProcess.cpudone + ")");

          blockedProcesses.put(currProcess.processNumber, comptime + currProcess.blockingDuration);
          currProcess.ionext = 0;
          currProcess.currQuantum = 0;
          currProcess.numblocked++;
        }

        if (processes.size() > 1
                && currProcess.currQuantum == quantum) {

          do {
            currProcessIndex = lottery.run(processes);
            currProcess = processes.elementAt(currProcessIndex);
          } while (blockedProcesses.containsKey(currProcess.processNumber));
          currProcess.currQuantum = 0;
          out.println("Process: " + currProcess.processNumber + " registered... ("
                  + currProcess.cputime + " " + currProcess.ioblocking + " " + currProcess.cpudone + ")");
        }

        Set<Integer> processesToUnblock = new HashSet<>();
        for (Integer processNumber : blockedProcesses.keySet()) {
          if (blockedProcesses.get(processNumber) == comptime) {
            processesToUnblock.add(processNumber);
          }
        }

        for (Integer processNumber : processesToUnblock) {
          blockedProcesses.remove(processNumber);
          out.println("Process: " + processNumber + " I/O unblocked...");
        }

        currProcess.cpudone++;
        currProcess.currQuantum++;
        currProcess.ionext++;
        comptime++;
      }
      out.close();
    } catch (IOException e) {/* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
