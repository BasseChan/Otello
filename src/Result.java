import java.util.List;

public class Result {
    private int value;
    private List<Position> changed;

    public Result() {
    }

    public Result(int value, List<Position> changed) {
        this.value = value;
        this.changed = changed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Position> getChanged() {
        return changed;
    }

    public void setChanged(List<Position> changed) {
        this.changed = changed;
    }
}
