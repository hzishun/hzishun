package Data;

import java.io.*;

public class Input {
    private File file;

    public Input(File file) {
        this.file = file;
    }

    public Problem getProblemDesFromFile(){
        Problem input = new Problem();
        BufferedReader reader = getBufferedReader(file);

        try{
            

        }catch(IOException e){
            e.printStackTrace();
        }

        return input;
    }

    private BufferedReader getBufferedReader(File file) {
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return reader;
    }

}
