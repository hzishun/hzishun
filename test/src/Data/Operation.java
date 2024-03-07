package Data;

public class Operation {
    public int jobNo;
    public int task;
    public int startTime;
    public int endTime;
    public int aStartTime;
    public int machineNo;
    public int span;
    public String id;

    public Operation(){
        jobNo = -1;
        task = -1;
        startTime = -1;
        endTime = -1;
        aStartTime = -1;
        machineNo = -1;
        span = -1;
    }

    public Operation(Operation o){
        this.id = o.id;
        this.span = o.span;
        this.jobNo = o.jobNo;
        this.task = o.task;
        this.startTime = o.startTime;
        this.endTime = o.endTime;
        this.aStartTime = o.aStartTime;
        this.machineNo = o.machineNo;
    }

    public void initOperation(){
        jobNo = -1;
        task = -1;
        startTime = -1;
        endTime = -1;
        aStartTime = -1;
        machineNo = -1;
        span = -1;
    }

}
