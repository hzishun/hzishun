package Data;

public class Problem {
    private int[] machineCountArr;

    private int[] operationCountArr;

    private int[][] proDesMatrix;

    private int machineCount;

    private int jobCount;

    private int maxOperationCount;

    private int totalOperationCount;

    private int[][] operationToIndex;

    public int[] getMachineCountArr(){
        return machineCountArr;
    }

    public void setMachineCountArr(int[] machineCountArr){
        this.machineCountArr = machineCountArr;
    }

    public int[] getOperationCountArr(){
        return operationCountArr;
    }

    public void setOperationCountArr(int[] operationCountArr){
        this.operationCountArr = operationCountArr;
    }

    public int[][] getOperationToIndex(){
        return operationToIndex;
    }

    public void setOperationToIndex(int[][] operationToIndex){
        this.operationToIndex = operationToIndex;
    }

    public int getMaxOperationCount(){
        return maxOperationCount;
    }

    public void setMaxOperationCount(int maxOperationCount){
        this.maxOperationCount = maxOperationCount;
    }

    public int[][] getProDesMatrix(){
        return proDesMatrix;
    }

    public void setProDesMatrix(int[][] proDesMatrix){
        this.proDesMatrix = proDesMatrix;
    }

    public int getMachineCount(){
        return machineCount;
    }

    public void setMachineCount(int machineCount){
        this.machineCount = machineCount;
    }

    public int getJobCount(){
        return jobCount;
    }

    public void setJobCount(int jobCount){
        this.jobCount = jobCount;
    }

    public int getTotalOperationCount(){
        return totalOperationCount;
    }

    public void setTotalOperationCount(int totalOperationCount){
        this.totalOperationCount = totalOperationCount;
    }
}
