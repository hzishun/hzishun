package Main;

import Data.Input;
import Data.Problem;
import Data.Solution;
import GAlgorithm.MyHybridAlgorithm;

import java.io.File;

public class main {
    public static void main(String[] args) {
        Input input = new Input(new File("C:\\Users\\Admin\\Desktop\\test(1)(1)\\test(1)\\test\\test\\src\\Main\\Mk09.txt"));
        Problem p = input.getProdesFromFile();

        MyHybridAlgorithm GA = new MyHybridAlgorithm(p);
        Solution GABest = GA.solve();

    }
}
