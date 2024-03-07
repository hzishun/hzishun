package GAlgorithm;

import Data.Job;
import Data.Operation;
import Data.Problem;
import Data.Solution;

import java.util.*;

public class MyHybridAlgorithm {

    private Operation[][] operationMatrix;
    private Problem input;
    private Random r;
    private int maxGen = 500;
    private int popSize = 50;
    private double pr = 0.10;
    private double pc = 0.80;
    private final double pm = 0.10;
    private final double pp = 0.30;
    private double GSProbability = 0.1;
    private double LSProbability = 0.1;
    private double RSProbability = 0.8;

    public MyHybridAlgorithm(Problem input){
        this.input = input;

        this.operationMatrix = new Operation[input.getJobCount()][];
        for(int i = 0; i < input.getJobCount(); i++){
            operationMatrix[i] = new Operation[input.getOperationCountArr()[i]];
            for(int j = 0; j < operationMatrix[i].length; j++){
                operationMatrix[i][j] = new Operation();
            }
        }
        this.r = new Random();

    }

    public Solution solve(){
        CaculateFitness c = new CaculateFitness();
        ChromosomeOperation chromosomeOperation = new ChromosomeOperation(input, r);

        int jobCount = input.getJobCount();
        Job[] jobs = new Job[jobCount];
        int[][] operationToIndex = input.getOperationToIndex();
        for(int i = 0; i < jobCount; i++){
            int opsNr = input.getOperationCountArr()[i];
            int[] opsIndex = operationToIndex[i];
            int[] opsMacNr = new int[opsNr];
            for(int j = 0; j < opsNr; j++){
                opsMacNr[j] = input.getMachineCountArr()[opsIndex[j] - 1];
            }
            jobs[i] = new Job(i, opsNr, opsIndex, opsMacNr);
//            jobs[i].printJob();
        }

        long startTime = System.currentTimeMillis();

        Chromosome[] parents;

//        for(int i = 0; i < popSize; i++){
//            parents[i] = new Chromosome(jobs, r);
//            parents[i].fitness = 1.0 / c.evaluate(parents[i], input, operationMatrix);
////            parents[i].printChromosome();
//        }

        parents = chromosomeOperation.initChromosome(jobs, popSize, GSProbability, LSProbability);
//        parents = chromosomeOperation.initChromosome1(jobs, popSize);
        for(int i = 0; i < popSize; i++){
            parents[i].makespan = c.evaluate(parents[i], input, operationMatrix);
            parents[i].fitness = 1.0 / parents[i].makespan;
//            parents[i].printChromosome();
        }

        Chromosome[] children = new Chromosome[popSize];
        for(int i = 0; i < popSize; i++){
            children[i] = new Chromosome(parents[i]);
        }

        double maxFitness = Double.NEGATIVE_INFINITY;
        int maxFitnessIndex = 0;
        for(int i = 0; i < popSize; i++){
            if (maxFitness < parents[i].fitness){
                maxFitnessIndex = i;
                maxFitness = parents[i].fitness;
            }
        }
        Chromosome bestChromosome = new Chromosome(parents[maxFitnessIndex]);
        Chromosome currentBestChromosome = new Chromosome(parents[maxFitnessIndex]);

        int gen = 0;
        int noImprove = 0;
        while(gen < this.maxGen){
            int maxStagnantStep = 30;
            if(gen - noImprove > maxStagnantStep){
                int num = (int) (pp * popSize);
                ArrayList<Chromosome> p = new ArrayList<>();
                Collections.addAll(p, parents);
                Collections.sort(p);
                for(int i = 0; i < num; i++){
                    parents[i] = p.get(i);
                }
                for(int i = num; i < popSize; i++){
                    parents[i] = new Chromosome(jobs, r);
                    parents[i].makespan = c.evaluate(parents[i], input, operationMatrix);
                    parents[i].fitness = 1.0 / parents[i].makespan;
                }
                noImprove = gen;
            }

            children = chromosomeOperation.Selection(parents, pr);
            children = chromosomeOperation.CrossOver(children, pc);
            children = chromosomeOperation.Mutation(children);

            currentBestChromosome = getBest(children);
            if(bestChromosome.fitness < currentBestChromosome.fitness){
                bestChromosome = new Chromosome(currentBestChromosome);
                noImprove = gen;
                System.out.println("In " + gen + " generation, find new best fitness is:" + currentBestChromosome.fitness + ", makespan:" + currentBestChromosome.makespan);
            }
            System.out.println(" After " + gen + " generation, the best fitness is:" + bestChromosome.fitness + ", makespan:" + currentBestChromosome.makespan);
            gen++;
        }

        Solution bestSolution = new Solution(operationMatrix, bestChromosome, input, bestChromosome.makespan);

        long endTime = System.currentTimeMillis();
        System.out.println(" 算法时间花费：" + (endTime - startTime) / 1000.0 + "s");

        return bestSolution;
    }

    public Chromosome getBest(Chromosome[] chromosomes){
        int popSize = chromosomes.length;
        int index = 0;
        double maxFitness = -1;

        for(int i = 0; i < popSize; i++){
            if(maxFitness < chromosomes[i].fitness){
                maxFitness = chromosomes[i].fitness;
                index = i;
            }
        }
        return chromosomes[index];
    }
}
