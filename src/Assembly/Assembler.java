package Assembly;

import Memory.Registers;

public class Assembler {

    public static void main(String[] args) {
        Fetch fetch = new Fetch(new Registers());
        fetch.startFetch();
    }
}
