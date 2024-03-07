package GAlgorithm;

import Data.Operation;
import Data.Problem;

import java.util.ArrayList;
import java.util.Arrays;

public class CaculateFitness {

    public class machineTime{
        int start;
        int end;
        int type;

        machineTime(int s, int e, int type){
            this.start = s;
            this.end = e;
            this.type = type;//0 空闲；1 加工
        }
        String test0(){
            String str;
            str = "Start:" + this.start + "," + "end:" + this.end + "," + "type:" + this.type;
            return str;
        }
    }

    public CaculateFitness(){
    }

    public static int[][][] getMachineNoAndTime(Problem input, int[] MS){
        int[][][] MachineNoAndTime = new int[2][input.getJobCount()][input.getMaxOperationCount()];
        int[][] MachineNo = new int[input.getJobCount()][input.getMaxOperationCount()];
        int[][] MachineTime = new int[input.getJobCount()][input.getMaxOperationCount()];
        int[][] proDesMatrix = input.getProDesMatrix();
        int[][] operationToIndex = input.getOperationToIndex();

        for(int i = 0; i < input.getJobCount(); i++){
            for(int j = 0; j < input.getMaxOperationCount(); j++){
                if(operationToIndex[i][j] != 0){
                    int tempCount = 0;
                    int index = 0;
                    while(tempCount < MS[operationToIndex[i][j] - 1]){
                        if(proDesMatrix[operationToIndex[i][j] - 1][index] != 0){
                            tempCount++;
                        }
                        index++;
                    }
                    index--;
                    MachineNo[i][j] = index;
                    MachineTime[i][j] = proDesMatrix[operationToIndex[i][j] - 1][index];
                }else{
                    MachineNo[i][j] = -1;
                    MachineTime[i][j] = -1;
                }
            }
        }
        MachineNoAndTime[0] = MachineNo;
        MachineNoAndTime[1] = MachineTime;
        return MachineNoAndTime;
    }

    public int evaluate(Chromosome chromosome, Problem input, Operation[][] operationMatrix){

        int jobNo;
        int operNo;
        int machineCount = input.getMachineCount();
        int[] operationNoOfEachJob = new int[input.getJobCount()];
        Arrays.fill(operationNoOfEachJob, 0);

        int[][][] machineNoAndTime = getMachineNoAndTime(input, chromosome.gene_MS);
        int[][] machineNoArr = machineNoAndTime[0];
        int[][] machineTimeArr = machineNoAndTime[1];

        ArrayList<machineTime>[] machineTimes = new ArrayList[input.getMachineCount()];
        for(int i = 0; i < machineCount; i++ ){
            machineTimes[i] = new ArrayList<>();
            machineTimes[i].add(new machineTime(0, Integer.MAX_VALUE, 0));
        }

        for(int i = 0; i < chromosome.gene_OS.length; i++){
            jobNo = chromosome.gene_OS[i];
            operNo = operationNoOfEachJob[chromosome.gene_OS[i]]++;

            int machineNo = machineNoArr[jobNo][operNo];
            int operationTime = machineTimeArr[jobNo][operNo];

            if(operNo == 0){
                operationMatrix[jobNo][operNo].aStartTime = 0;
            }else{
                operationMatrix[jobNo][operNo].aStartTime = operationMatrix[jobNo][operNo - 1].endTime;
            }

            operationMatrix[jobNo][operNo].jobNo = jobNo;
            operationMatrix[jobNo][operNo].task = operNo;
            operationMatrix[jobNo][operNo].machineNo = machineNo;
            operationMatrix[jobNo][operNo].span = operationTime;

            for(int j = 0; j < machineTimes[machineNo].size(); j++){
                int start = Math.max(machineTimes[machineNo].get(j).start, operationMatrix[jobNo][operNo].aStartTime);
                int end = start + operationTime;

                if(machineTimes[machineNo].get(j).type == 0 && end <= machineTimes[machineNo].get(j).end){
                    operationMatrix[jobNo][operNo].startTime = start;
                    operationMatrix[jobNo][operNo].endTime = end;

                    ArrayList<machineTime> t = new ArrayList<>();
                    if(machineTimes[machineNo].get(j).start < operationMatrix[jobNo][operNo].aStartTime){
                        t.add(new machineTime(machineTimes[machineNo].get(j).start, operationMatrix[jobNo][operNo].startTime, 0));
                        t.add(new machineTime(operationMatrix[jobNo][operNo].aStartTime, end, 1));
                    }else{
                        t.add(new machineTime(machineTimes[machineNo].get(j).start, end, 1));
                    }

                    if(end < machineTimes[machineNo].get(j).end){
                        t.add(new machineTime(end, machineTimes[machineNo].get(j).end, 0));
                    }

                    machineTimes[machineNo].remove(j);
                    machineTimes[machineNo].addAll(j, t);

                    break;
                }
            }


        }

        int makeSpan = 0;
        for (int i = 0; i < machineCount; i++){
            makeSpan = Math.max(makeSpan, machineTimes[i].get(machineTimes[i].size() - 1).start);
        }
        //test makespan
//        System.out.println(Arrays.toString(chromosome.gene_OS));
//        System.out.println(Arrays.toString(chromosome.gene_MS));
//        System.out.println();
//        for(int i = 0; i < machineCount; i++){
//            String[] str = new String[machineTimes[i].size()];
//            for(int j = 0; j < machineTimes[i].size(); j++){
//                str[j] = machineTimes[i].get(j).test0();
//            }
//            System.out.println(Arrays.toString(str));
//        }
//        System.out.println(makeSpan);

        return makeSpan;
    }

}
