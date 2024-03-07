package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Input {
    private File file;

    public Input(File file){
        this.file = file;
    }

    public Problem getProdesFromFile(){
        Problem Input = new Problem();
        BufferedReader reader = readFileByLines(file);
        String proDesString;
        String[] proDesArr = null;
        List<Integer> operationCountList = new ArrayList<Integer>();
//        readFileByChars(file);

        try{
            proDesString = reader.readLine();
            proDesArr = proDesString.split("\\s+");
            int jobNum = Integer.valueOf(proDesArr[0]);
            int machineNum = Integer.valueOf(proDesArr[1]);
            
            int[][] proData = new int[jobNum][];
            for(int i = 0; i < jobNum; i++){
                proDesArr = reader.readLine().trim().split("\\s+");
                proData[i] = conversion(proDesArr);

//                System.out.println(Arrays.toString(proData[i]));
            }

            int maxOperationCount = 0; int count = 0;
            for(int i = 0; i < jobNum; i++){
                count += proData[i][0];
                if(maxOperationCount < proData[i][0]){
                    maxOperationCount = proData[i][0];
                }
            }

            int[][] operationToIndex = new int[jobNum][maxOperationCount];
            int[][] proDesMatrix = new int[count][machineNum];
            int tempCount = 1;

            for(int i = 0; i < jobNum; i++){
                int countPin = 1;
                for(int j = 0; j < maxOperationCount; j++){

                    if(j < proData[i][0]){
                        operationToIndex[i][j] = tempCount;
                        operationCountList.add(proData[i][countPin]);
                        for(int k = 1; k <= proData[i][countPin]; k++){
                            proDesMatrix[tempCount - 1][proData[i][countPin + 2*k - 1] - 1] = proData[i][countPin + 2*k];
                        }
                        countPin += 2 * proData[i][countPin] + 1;
                        tempCount++;
                    }

                }
            }

            int[] machineCountArr = new int[operationCountList.size()];
            for(int i = 0; i < operationCountList.size(); i++){
                machineCountArr[i] = operationCountList.get(i);
            }

            int[] operationCountArr = new int[jobNum];
            for(int i = 0; i < jobNum; i++){
                operationCountArr[i] = proData[i][0];
            }

            Input.setMachineCountArr(machineCountArr);
            Input.setOperationCountArr(operationCountArr);
            Input.setOperationToIndex(operationToIndex);
            Input.setMaxOperationCount(maxOperationCount);
            Input.setProDesMatrix(proDesMatrix);
            Input.setMachineCount(machineNum);
            Input.setJobCount(jobNum);
            Input.setTotalOperationCount(count);
        }catch (IOException e){
            e.printStackTrace();
        }
        return Input;
    }

    public static void readFileByChars(File file){
        Reader reader = null;
        try{
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while((tempchar = reader.read()) != -1){
                if(((char)tempchar) != '\r'){
                    System.out.print((char)tempchar);
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public BufferedReader readFileByLines(File file){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));

//            String tempString = null;
//            int line = 1;
//            while((tempString = reader.readLine()) != null){
//                System.out.println("line" + line + ":" + tempString);
//                line++;
//            }
//            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
//        finally {
//            if (reader != null){
//                try{
//                    reader.close();
//                }catch (IOException e1){
//                    e1.printStackTrace();
//                }
//            }
//        }
        return reader;
    }

    public int[] conversion(String[] string1){
        int[] integer1 = new int[string1.length];
        for (int i = 0; i < string1.length; i++){
            integer1[i] = Integer.valueOf(string1[i]);
        }
        return integer1;
    }

}
