package Data;

import java.util.Arrays;

public class Job {

    public int index;

    public int opsNr;

    public int[] opsIndex;

    public int[] opsMacNr;

    public Job(int index, int opsNr, int[] opsIndex, int[] opsMacNr){
        this.index = index;
        this.opsNr = opsNr;
        this.opsIndex = opsIndex;
        this.opsMacNr = opsMacNr;
    }

    public void printJob(){
        System.out.println(index);
        System.out.println(opsNr);
        System.out.println(Arrays.toString(opsIndex));
        System.out.println(Arrays.toString(opsMacNr));
    }
}
