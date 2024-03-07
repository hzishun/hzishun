package GAlgorithm;

import Data.Job;
import Data.Problem;

import java.util.*;

public class ChromosomeOperation {

    private Problem input;
    private Random r;

    public ChromosomeOperation(Problem input, Random r){
        this.input = input;
        this.r = r;
    }

    public Chromosome[] initChromosome(Job[] jobs, int popSize, double GSProbability, double LSProbability){
        int GSNum = (int) Math.floor(GSProbability * popSize);
        int LSNum = (int) Math.floor((LSProbability + GSProbability) * popSize);

        Chromosome[] parents = new Chromosome[popSize];

        for(int i = 0; i < GSNum; i++){
            parents[i] = globalSelection();
        }
        for(int j = GSNum; j < LSNum; j++){
            parents[j] = localSelection();
        }
        for(int k = LSNum; k <popSize; k++){
            parents[k] = randomSelection(jobs);
        }

        return parents;
    }

    public Chromosome[] initChromosome1(Job[] jobs, int popSize){
        Chromosome[] parents = new Chromosome[popSize];

        for(int i = 0; i < popSize; i++){
            parents[i] = randomSelection(jobs);
        }
        return parents;
    }

    public Chromosome globalSelection(){
        int jobCount = input.getJobCount();
        int machineCount = input.getMachineCount();
        int count = 0;
        int[][] operationToIndex = input.getOperationToIndex();
        int[][] proDesMatrix = input.getProDesMatrix();
        int[] gene_OS = new int[input.getTotalOperationCount()];
        int[] gene_MS = new int[input.getTotalOperationCount()];

        int[] totalMachineLoad = new int[machineCount];

        ArrayList<Integer> jobSet = new ArrayList<>();
        for(int l = 0; l < jobCount; l++){
            jobSet.add(l);
        }
        Collections.shuffle(jobSet, r);

        for(int i = 0; i < jobCount; i++){
            for(int j = 0; j < input.getOperationCountArr()[jobSet.get(i)]; j++){
                int[] tempMatrix = new int[machineCount];
                int[] sumMatrix = new int[machineCount];
                int index = 0;
                int minMachineLoad = Integer.MAX_VALUE;
                System.arraycopy(proDesMatrix[operationToIndex[jobSet.get(i)][j] - 1], 0, tempMatrix, 0, machineCount);

                for(int k = 0; k < machineCount; k++){
                    if(tempMatrix[k] == 0){
                        tempMatrix[k] = Integer.MAX_VALUE;
                        sumMatrix[k] = tempMatrix[k] + totalMachineLoad[k];
                    }else{
                        sumMatrix[k] = tempMatrix[k] + totalMachineLoad[k];
                        if(sumMatrix[k] < minMachineLoad){
                            minMachineLoad = sumMatrix[k];
                            index = k;
                        }
                    }
                }
                totalMachineLoad[index] = sumMatrix[index];

                gene_OS[count] = jobSet.get(i);
                int tempCount = 0;
                int iCount = 0;
                while(tempCount <= index){
                    if(proDesMatrix[operationToIndex[jobSet.get(i)][j] - 1][tempCount] != 0){
                        iCount++;
                    }
                    tempCount++;
                }
                gene_MS[operationToIndex[jobSet.get(i)][j] - 1] = iCount;
                count++;
            }
        }
//        System.out.println(Arrays.toString(gene_OS));
//        System.out.println(Arrays.toString(gene_MS));
        return new Chromosome(gene_OS, gene_MS, r);
    }

    public Chromosome localSelection(){
        int jobCount = input.getJobCount();
        int machineCount = input.getMachineCount();
        int count = 0;
        int[][] operationToIndex = input.getOperationToIndex();
        int[][] proDesMatrix = input.getProDesMatrix();
        int[] gene_OS = new int[input.getTotalOperationCount()];
        int[] gene_MS = new int[input.getTotalOperationCount()];

        ArrayList<Integer> jobSet = new ArrayList<>();
        for(int l = 0; l < jobCount; l++){
            jobSet.add(l);
        }
        Collections.shuffle(jobSet, r);

        for(int i = 0; i < jobCount; i++){
            int[] totalMachineLoad = new int[machineCount];
            for(int j = 0; j < input.getOperationCountArr()[jobSet.get(i)]; j++){
                int[] tempMatrix = new int[machineCount];
                int[] sumMatrix = new int[machineCount];
                int index = 0;
                int minMachineLoad = Integer.MAX_VALUE;
                System.arraycopy(proDesMatrix[operationToIndex[jobSet.get(i)][j] - 1], 0, tempMatrix, 0, machineCount);

                for(int k = 0; k < machineCount; k++){
                    if(tempMatrix[k] == 0){
                        tempMatrix[k] = Integer.MAX_VALUE;
                        sumMatrix[k] = tempMatrix[k] + totalMachineLoad[k];
                    }else{
                        sumMatrix[k] = tempMatrix[k] + totalMachineLoad[k];
                        if(sumMatrix[k] < minMachineLoad){
                            minMachineLoad = sumMatrix[k];
                            index = k;
                        }
                    }
                }
                totalMachineLoad[index] = sumMatrix[index];

                gene_OS[count] = jobSet.get(i);
                int tempCount = 0;
                int iCount = 0;
                while(tempCount <= index){
                    if(proDesMatrix[operationToIndex[jobSet.get(i)][j] - 1][tempCount] != 0){
                        iCount++;
                    }
                    tempCount++;
                }
                gene_MS[operationToIndex[jobSet.get(i)][j] - 1] = iCount;
                count++;
            }
        }
//        System.out.println(Arrays.toString(gene_OS));
//        System.out.println(Arrays.toString(gene_MS));
        return new Chromosome(gene_OS, gene_MS, r);
    }

    public Chromosome randomSelection(Job[] entries){
        ArrayList<Integer> os = new ArrayList<>();
        for(int i = 0; i < entries.length; i++){
            for(int j = 0; j < entries[i].opsNr; j++){
                os.add(entries[i].index);
            }
        }
        Collections.shuffle(os, r);

        ArrayList<Integer> ms = new ArrayList<>();
        for(int i = 0; i < entries.length; i++){
            for(int j = 0; j < entries[i].opsNr; j++){
                ms.add(r.nextInt(entries[i].opsMacNr[j]) + 1);
            }
        }

        int[] gene_OS = new int[os.size()];
        for(int i = 0; i < os.size(); i++){
            gene_OS[i] = os.get(i);
        }
        int[] gene_MS = new int[ms.size()];
        for(int i = 0; i < ms.size(); i++){
            gene_MS[i] = ms.get(i);
        }
        return new Chromosome(gene_OS, gene_MS, r);
    }

    public Chromosome[] rouletteWheelSelection(Chromosome[] parents){
        int popSize = parents.length;
        double[] probability = new double [popSize];
        double sumFitness = 0;
        double sumProbability = 0;
        Chromosome[] children = new Chromosome[popSize];

        for(int i = 0; i < popSize; i++){
            sumFitness += parents[i].fitness;
        }

        for(int j = 0; j < popSize; j++){
            sumProbability += parents[j].fitness / sumFitness;
            probability[j] = sumProbability;
        }

        for(int k = 0; k < popSize; k++){
            double randomNum = r.nextDouble();
            for(int l = 0; l < popSize; l++){
                if(randomNum < probability[l]){
                    children[k] = new Chromosome(parents[l]);
                    break;
                }
            }
        }

        return children;
    }

    public Chromosome[] tournamentSelection(Chromosome[] parents){
        int popSize = parents.length;
        Chromosome[] children = new Chromosome[popSize];

        for(int i = 0; i < popSize; i++){
            int selection1 = r.nextInt(popSize);
            int selection2 = r.nextInt(popSize);
            if(parents[selection1].fitness < parents[selection2].fitness){
                children[i] = new Chromosome(parents[selection2]);
            }else{
                children[i] = new Chromosome(parents[selection1]);
            }
        }
        return children;
    }

    public Chromosome[] elitistSelection(Chromosome[] parents, double pr){
        int popSize = parents.length;
        int Num = (int) (popSize * pr);
        Chromosome[] children = new Chromosome[Num];

        ArrayList<Chromosome> p = new ArrayList<>();
        Collections.addAll(p, parents);
        Collections.sort(p);

        for(int i = 0; i < Num; i++){
            children[i] = p.get(i);
        }
        return children;
    }

    public Chromosome[] Selection(Chromosome[] parents, double pr){
        int popNr = parents.length;
        int Num = (int) (popNr * pr);
        Chromosome[] children = new Chromosome[popNr];

        ArrayList<Chromosome> p = new ArrayList<>();
        Collections.addAll(p, parents);
        Collections.sort(p);
        for(int i = 0; i < Num; i++){
            children[i] = new Chromosome(p.get(i));
        }

        for(int j = Num; j < popNr; j++){
            int selection1 = r.nextInt(popNr);
            int selection2 = r.nextInt(popNr);
            if(parents[selection1].fitness < parents[selection2].fitness){
                children[j] = parents[selection2];
            }else{
                children[j] = parents[selection1];
            }
        }
        return children;
    }

    public Chromosome[] CrossOver(Chromosome[] parents, double pc){
        int popSize = parents.length;

        for(int i = 0; i < popSize; i += 2){
            double randomNum = r.nextDouble();
            if(randomNum < pc){
                double randomNum1 = r.nextDouble();
                if(randomNum1 < 0.5) {
                    JBXCrossOver(parents[i].gene_OS, parents[i + 1].gene_OS);
                }else{
                    POXCrossOver(parents[i].gene_OS, parents[i + 1].gene_OS);
                }
                uniformCrossOver_machine(parents[i].gene_MS, parents[i + 1].gene_MS);
            }
        }

        return parents;
    }

    public void POXCrossOver(int[] o1, int[] o2){
        int popSize = o1.length;
        int jobCount = input.getJobCount();
        int randomNum = r.nextInt(jobCount - 1) + 1;
        int[] t1 = new int[popSize];
        int[] t2 = new int[popSize];

        System.arraycopy(o1, 0, t1, 0, popSize);
        System.arraycopy(o2, 0, t2, 0, popSize);
        Arrays.fill(o1, -1);
        Arrays.fill(o2, -1);

        ArrayList<Integer> s1 = new ArrayList<>();
        ArrayList<Integer> s2 = new ArrayList<>();

        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < jobCount; i++){
            p.add(i);
        }
        Collections.shuffle(p);

        List<Integer> p1 = p.subList(0, randomNum);

        for(int j = 0; j < popSize; j++){
            if(p1.contains(t1[j])){
                o1[j] = t1[j];
            }else{
                s1.add(t1[j]);
            }
            if(p1.contains(t2[j])){
                o2[j] = t2[j];
            }else{
                s2.add(t2[j]);
            }
        }

        int index1 = 0;
        int index2 = 0;
        for(int k = 0; k < popSize; k++){
            if(o1[k] == -1){
                o1[k] = s2.get(index1);
                index1++;
            }
            if(o2[k] == -1){
                o2[k] = s1.get(index2);
                index2++;
            }
        }
    }

    public void JBXCrossOver(int[] o1, int[] o2){
        int popSize = o1.length;
        int jobCount = input.getJobCount();
        int randomNum = r.nextInt(jobCount - 1) + 1;
        int[] t1 = new int[popSize];
        int[] t2 = new int[popSize];

        System.arraycopy(o1, 0, t1, 0, popSize);
        System.arraycopy(o2, 0, t2, 0, popSize);
        Arrays.fill(o1, -1);
        Arrays.fill(o2, -1);

        ArrayList<Integer> s1 = new ArrayList<>();
        ArrayList<Integer> s2 = new ArrayList<>();

        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < jobCount; i++){
            p.add(i);
        }
        Collections.shuffle(p);

        List<Integer> p1 = p.subList(0, randomNum);
        List<Integer> p2 = p.subList(randomNum, jobCount);

        for(int j = 0; j < popSize; j++){
            if(p1.contains(t1[j])){
                o1[j] = t1[j];
                s1.add(t1[j]);
            }
            if(p2.contains(t2[j])){
                o2[j] = t2[j];
                s2.add(t2[j]);
            }
        }

        int index1 = 0;
        int index2 = 0;
        for(int k = 0; k < popSize; k++){
            if(o1[k] == -1){
                o1[k] = s2.get(index1);
                index1++;
            }
            if(o2[k] == -1){
                o2[k] = s1.get(index2);
                index2++;
            }
        }
    }

    public void uniformCrossOver_machine(int[] m1, int[] m2){
        int popSize = m1.length;
        int crossNum = r.nextInt((int) (popSize * 0.25), (int) (popSize * 0.45));
        int temp;
        Random r = new Random();

        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < crossNum; i++) {
            int randomNum = r.nextInt(popSize);
            while(p.contains(randomNum)) {
                randomNum = r.nextInt(popSize);
            }
            p.add(randomNum);
        }

        for(int j = 0; j < crossNum; j++){
            temp = m1[p.get(j)];
            m1[p.get(j)] = m2[p.get(j)];
            m2[p.get(j)] = temp;
            }
        }

    public Chromosome[] Mutation(Chromosome[] chromosome) {
        int popSize = chromosome.length;
        for(int i = 0; i < popSize; i++){
            double posibility = r.nextDouble();
            if (posibility < 0.5) {
                operSeqMutationSwap(chromosome[i].gene_OS);
            } else {
                operSeqMutationNeighbor(chromosome[i].gene_OS);
            }

            machineSeqMutation(chromosome[i].gene_MS);
        }
        return chromosome;
    }

    public void operSeqMutationSwap(int[] os) {
        int len = os.length;
        // 随机查找两点
        double posibility = r.nextDouble();
        if (posibility < 0.5) {
            int posa = r.nextInt(len);
            int posb = r.nextInt(len);
            while (posa == posb)
                posb = r.nextInt(len);
            int temp;
            if (posa > posb) {
                temp = posa;
                posa = posb;
                posb = temp;
            }

            temp = os[posa];
            os[posa] = os[posb];
            os[posb] = temp;
        }
    }

    public void operSeqMutationNeighbor(int[] os) {
        int len = os.length;
        int pos1 = r.nextInt(len);
        int pos2 = r.nextInt(len);
        while (os[pos1] == os[pos2])
            pos2 = r.nextInt(len);
        int pos3 = r.nextInt(len);
        while (os[pos3] == os[pos2] || os[pos3] == os[pos1])
            pos3 = r.nextInt(len);

        ArrayList<Integer> li = new ArrayList<>();
        li.add(os[pos1]);
        li.add(os[pos2]);
        li.add(os[pos3]);
        Collections.shuffle(li);

        os[pos1] = li.get(0);
        os[pos2] = li.get(1);
        os[pos3] = li.get(2);
    }

    public int[] machineSeqMutation(int[] ms) {
        int[] machineCountArr = input.getMachineCountArr();
        for (int i = 0; i < ms.length / 2; i++) {
            int pos = r.nextInt(ms.length);
            ms[pos] = r.nextInt(machineCountArr[pos]) + 1;
        }

        return ms;
    }

    }




