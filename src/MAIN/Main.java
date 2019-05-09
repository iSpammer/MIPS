package MAIN;
import Assembly.Fetch;
import Memory.Registers;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Registers REG = new Registers ();
        Fetch as = new Fetch(REG);
        System.out.println(Arrays.deepToString((REG.REG)));
        as.startFetch();
        System.out.println(Arrays.deepToString((REG.REG)));

    }
}
