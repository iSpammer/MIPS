package Assembly;

import ControlUnit.CU;
import Memory.Memory;
import Memory.Registers;

import java.util.Arrays;

public class Fetch {
    public static int pc = 0; //address next instruction
    private String ir = ""; //current instruction format
    private String irNxt = ""; //next instruction format
    private int currentMem = 1000; //index of current instruction in memory
    public Registers REG;
    public Boolean busy = false; //pipeline
    public Decode decode;
    public Execute execute;

    public Fetch(Registers REG) {
        this.REG = REG;
    }

    public void startFetch() {
        System.out.println("fetching ");
        if (!busy) {
            for (int i = 0; i < Memory.MEM.length && !(Memory.MEM[i][1].equalsIgnoreCase("00000000000000000000000000000000")); i += 2) {
                currentMem = 1000 + pc;
                System.out.println("fetching " + currentMem + ", " + pc + ", " + Memory.MEM[i][0]);
                if (Integer.parseInt(Memory.MEM[i][0]) == currentMem || Integer.parseInt(Memory.MEM[i][0]) == currentMem + 4) {
                    busy = true;
                    ir = Memory.MEM[i][1];
                    pc = pc + 4;
                    try {
                        irNxt = Memory.MEM[i + 1][1];
                        pc = pc + 4;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String opcode = ir.substring(0, 5);
                    if (opcode.equalsIgnoreCase("00000"))
                        CU.RegDst = true; //R-type
                    busy = false;

                    decode = new Decode(this, execute, REG, currentMem + "", ir, opcode);
                    decode.startDecode();
                    this.setDefaultState(); //reset all flags in control unit

                    while (decode.busy) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (irNxt.equalsIgnoreCase("00000000000000000000000000000000"))
                        return;
                    decode = new Decode(this, execute, REG, currentMem + "", irNxt, opcode);
                    decode.startDecode();
                    this.setDefaultState();
                }
            }
        }
    }

    public void setDefaultState() {
        CU.RegWrite = false;
        CU.ALUSrc = false;
        CU.Jump = false;
        CU.MemRead = false;
        CU.MemWrite = false;
        CU.RegDst = false;
        CU.ALUOp = 0000;
        CU.Branch = false;
        CU.MemToReg = false;
    }
}
