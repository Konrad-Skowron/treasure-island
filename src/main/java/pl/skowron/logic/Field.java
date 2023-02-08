package pl.skowron.logic;

public class Field {
    public FieldContent content;
    public int waitTime;
    public boolean isVisited;

    public Field(FieldContent content, int waitTime, boolean isVisited) {
        this.content = content;
        this.waitTime = waitTime;
        this.isVisited = isVisited;
    }
    public Field() {
        this.content = FieldContent.UNKNOWN;
        this.isVisited = false;
    }
}
