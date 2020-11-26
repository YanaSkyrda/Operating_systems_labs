package memorymanagement;

public class Page {
    int id;
    int physical;
    byte R;
    byte M;
    int inMemTime;
    int lastTouchTime;
    long high;
    long low;
    boolean lastUsedInReplacement;

    public Page(int id, int physical, byte R, byte M, int inMemTime, int lastTouchTime, long high, long low,
                boolean lastUsedInReplacement) {
        this.id = id;
        this.physical = physical;
        this.R = R;
        this.M = M;
        this.inMemTime = inMemTime;
        this.lastTouchTime = lastTouchTime;
        this.high = high;
        this.low = low;
        this.lastUsedInReplacement = lastUsedInReplacement;
    }

}
