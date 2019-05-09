package Assembly;

import ControlUnit.CU;
import Memory.Registers;

public class Decode {
    private Registers REG;
    private String currMem; //address current instruction in memory
    private String ir; //current instruction
    private String opcode; //current instruction
    public boolean busy = false;
    public Execute execute;
    private Fetch fetch;

    public void setIr(String nxtIr) {
        this.ir = nxtIr;
    }

    public Decode(Fetch fetch, Execute execute, Registers REG, String currentMem, String ir, String opcode) {
        this.REG = REG;
        this.currMem = currentMem;
        this.ir = ir;
        this.opcode = opcode;
        this.fetch = fetch;
        this.execute = execute;
    }


    public void startDecode() {
        System.out.println("decoding " + ir);
        if (!busy) {
            busy = true;
            int locRS = 0;
            int locRT = 0;
            String rs = ""; // for saving the opcode
            String rt = ""; // ^^
            String rd = ""; // ^^
            String shamt = "";
            String funct = "";
            opcode = ir.substring(0, 5);
            if (opcode.equals("00000")) {
                CU.RegDst = true;
            } else {
//            CU.RegDst = false;
            }
            rs = ir.substring(5, 10);
            rt = ir.substring(10, 15);
            for (int i = 0; i < REG.REG.length; i++) {
                if (rs.equalsIgnoreCase(REG.REG[i][0])) {
                    locRS = i;
//                System.out.println(locRS + " loc rs");
                }
                if (rt.equalsIgnoreCase(REG.REG[i][0])) {
                    locRT = i;
                }
            }

            if (execute != null) {
                while (execute.busy) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (CU.RegDst) {
                int locRD = 0;
                for (int i = 0; i < REG.REG.length; i++) {
//                    System.out.println(ir);
                    rd = ir.substring(15, 20);
                    shamt = ir.substring(20, 28);
                    funct = ir.substring(28, 32);
                    if (rd.equalsIgnoreCase(REG.REG[i][0])) {
                        locRD = i;
                    }
                }
                busy = false;
                execute = new Execute(fetch, REG, opcode, ir, funct, shamt, locRS, locRT, locRD);
                execute.startExecuteRType();

            } else {
                busy = false;
                execute = new Execute(fetch, REG, opcode, ir, currMem, locRT, locRS);
                execute.startExecuteIJType();
            }
        }
    }
}
