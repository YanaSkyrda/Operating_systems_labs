import java.util.Random;
import java.util.Vector;

public class Lottery {
    private long ticketsAmount;

    public Lottery(long ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public long getTicketsAmount() {
        return ticketsAmount;
    }

    public void setTicketsAmount(long ticketsAmount) {
        this.ticketsAmount = ticketsAmount;
    }

    public int run(Vector<Process> processes) {
        Random random = new Random();
        long ticket = 1 + (long) (Math.random() * ticketsAmount);
        long currTickets = 0;

        for (int i = 0; i < processes.size(); i++) {
            currTickets += processes.elementAt(i).ticketsAmount;
            if (ticket <= currTickets
                    && processes.elementAt(i).cpudone < processes.elementAt(i).cputime) {
                return i;
            }
        }
        return -1;
    }
}