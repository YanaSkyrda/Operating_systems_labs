public class Process {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int ticketsAmount;
  public int currQuantum;
  public int blockingDuration;
  public int processNumber;

  public Process(int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int blockingDuration,
                 int ticketsAmount, int currQuantum, int processNumber) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.blockingDuration = blockingDuration;
    this.ticketsAmount = ticketsAmount;
    this.currQuantum = currQuantum;
    this.processNumber = processNumber;
  } 	
}
