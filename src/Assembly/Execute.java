package Assembly;

import ControlUnit.CU;
import Memory.Memory;
import Memory.Registers;


public class Execute {
    private String ir; //current instruction
    private int locRS; //index of RS in reg array
    private int locRT; //index of RT in reg array
    private int locRD; //index of RD in reg array
    private String funct;
    private String shamt;
    private String currMem; //index of current instruction in memory
    private Registers REG;
    private String opcode; //ir
    private Fetch fetch;
    public boolean busy = false;
    public boolean memReadBusy = false;
    public boolean memWriteBusy = false;

    public Execute(Fetch fetch, Registers REG, String opcode, String ir, String funct, String shamt, int locRS, int locRT, int locRD) {
        this.fetch = fetch;
        this.REG = REG;
        this.opcode = opcode;
        this.ir = ir;
        this.funct = funct;
        this.shamt = shamt;
        this.locRS = locRS;
        this.locRT = locRT;
        this.locRD = locRD;
    }

    public Execute(Fetch fetch, Registers REG, String opcode, String ir, String currMem, int locRS, int locRT) { //I & J - type
        this.fetch = fetch;
        this.REG = REG;
        this.opcode = opcode;
        this.ir = ir;
        this.locRS = locRS;
        this.locRT = locRT;
        this.currMem = currMem; //index of current instruction in memory
    }

    public void startExecuteRType() {
        if (!busy) {
            busy = true;
            CU.RegWrite = true;
            CU.Jump = false;
            CU.MemWrite = false;
            CU.MemRead = false;
            CU.ALUSrc = false;
//        System.out.println("fn" + funct);
            String res = "";
            System.out.println(funct + " funct");
            if (funct.equalsIgnoreCase("0000")) {
//            System.out.println(locRSe + "aaaa");
//            System.out.println("rs add:" + REG.REG[locRSe][0] + " val: " + REG.REG[locRSe][1]);
//            System.out.println("rt add:" + REG.REG[locRTe][0] + " val: " + REG.REG[locRTe][1]);
//            System.out.println("IR: " + ir);
//                System.out.println(REG.getREGValue(locRS) + "............");
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.getREGValue(locRS), 2) + Integer.parseInt(REG.getREGValue(locRT), 2));
                CU.RegWrite = true;
//            System.out.println(Integer.parseInt(res, 2) + " answer bt3t add");
            } else if (funct.equalsIgnoreCase("0001")) {
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.getREGValue(locRS), 2) - Integer.parseInt(REG.getREGValue(locRT), 2));
            } else if (funct.equalsIgnoreCase("0010")) {
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.getREGValue(locRS), 2) / Integer.parseInt(REG.getREGValue(locRT), 2));
                // divison
            } else if (funct.equalsIgnoreCase("0011")) {
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.getREGValue(locRS), 2) & Integer.parseInt(REG.getREGValue(locRT), 2));
//            System.out.println(res + "answer bt3t el and");
            } else if (funct.equalsIgnoreCase("0100")) {
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.getREGValue(locRS), 2) | Integer.parseInt(REG.getREGValue(locRT), 2));
            } else if (funct.equalsIgnoreCase("0101")) {
                System.out.println("d5l el sll");
                res = Integer.toBinaryString(Integer.parseInt(REG.getREGValue(locRS), 2) << Integer.parseInt(shamt, 2));
                int l = res.length();
                for (int i = 0; i < 32 - l; i++) {
                    res = '0' + res;
//                    System.out.println(res.length());
                }

                CU.RegWrite = true;
            } else if (funct.equalsIgnoreCase("0110")) {
                res = Integer.toBinaryString(Integer.parseInt(REG.getREGValue(locRS), 2) >>> Integer.parseInt(shamt, 2));
                int l = res.length();
                for (int i = 0; i < 32 - l; i++) {
                    res = '0' + res;
//                System.out.println(res.length());
                }
                CU.RegWrite = true;
            } else if (funct.equalsIgnoreCase("0111")) {
                CU.Jump = true;
                CU.RegWrite = false;
            }
            busy = false;
//            System.out.println(res + "answer bt3t el " + funct);
            System.out.println("Writting enabled " + CU.RegWrite);
            if (CU.RegWrite) {
                writeReg(locRD, res);
            }
            if (CU.Jump)
                jumpRegister(REG.getREGValue(locRD));
            if (CU.MemWrite)
                if (!memWriteBusy) {
                    memWriteBusy = true;
                    memoryWrite(0, "");
                    memReadBusy = false;
                }
            if (CU.MemRead)
                if (!memReadBusy) {
                    memReadBusy = true;
                    memoryRead(0, 0);
                    memReadBusy = false;
                }
//        System.out.println("aaaaaaaaaaaaaaaaaa" + REG.REG[locRDe][1] + ", " + REG.REG[locRDe][1]);
        }
    }

    public void startExecuteIJType() {
        if (!busy) {
            busy = true;
            CU.RegWrite = false;
            CU.Jump = false;
            CU.MemWrite = false;
            CU.MemRead = false;
            CU.ALUSrc = true;

            String offset = ir.substring(15, 32);

            String res = "";
            int add = 0;
            if (opcode.equalsIgnoreCase("00001")) {
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.REG[locRS][1], 2) + Integer.parseInt(signExtender(offset), 2));
                CU.RegWrite = true;
            } else if (opcode.equalsIgnoreCase("00010")) {

                res = Integer.toBinaryString(
                        Integer.parseInt(REG.REG[locRS][1], 2) | Integer.parseInt(signExtender(offset), 2));
                int l = res.length();
                for (int i = 0; i < 32 - l; i++) {
                    res = '0' + res;
                }

                CU.RegWrite = true;
                //XORI
            } else if (opcode.equalsIgnoreCase("00011")) {
                String adash = not(REG.REG[locRS][1]);
                String bdash = not(signExtender(offset));
                String adashb = "";
                for (int i = 0; i < 32; i++) {
                    if (adash.charAt(i) == '1' && signExtender(offset).charAt(i) == '1') {
                        adashb = adashb + '1';
                    } else {
                        adashb = adashb + '0';
                    }
                }
                String bdasha = "";
                for (int i = 0; i < 32; i++) {
                    if (bdash.charAt(i) == '1' && REG.REG[locRT][1].charAt(i) == '1') {
                        bdasha = bdasha + '1';
                    } else {
                        bdasha = bdasha + '1';
                    }
                }
                for (int i = 0; i < 32; i++) {
                    if (adashb.charAt(i) == '1' || bdasha.charAt(i) == '1') {
                        res = res + '1';
                    } else {
                        res = res + '0';
                    }
                }
                CU.RegWrite = true;
                //ANDI
            } else if (opcode.equalsIgnoreCase("00100")) {
                CU.ALUSrc = true;
                res = Integer.toBinaryString(
                        Integer.parseInt(REG.REG[locRS][1], 2) & Integer.parseInt(signExtender(offset)));
                int l = res.length();
                for (int i = 0; i < 32 - l; i++) {
                    res = '0' + res;
                }
                CU.RegWrite = true;
                //load
            } else if (opcode.equalsIgnoreCase("00101")) {
                CU.ALUSrc = true;
                int offsetValue = Integer.parseInt(signExtender(offset), 2);
                int rsValue = Integer.parseInt(REG.REG[locRS][1], 2);
                add = offsetValue + rsValue;
                System.out.println(add + " yeload da");
                CU.MemRead = true;

                //memory write
            } else if (opcode.equalsIgnoreCase("00110")) {
                CU.RegWrite = true;

                String t = REG.REG[locRS][1];
                try {
                    REG.setREG(locRS, REG.REG[locRT][1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res = t;
            } else if (opcode.equalsIgnoreCase("00111")) {
                CU.ALUSrc = true;
                int offsetValue = Integer.parseInt(signExtender(offset), 2);
                int rsValue = Integer.parseInt(REG.REG[locRS][1], 2);
                add = offsetValue + rsValue;
                CU.MemWrite = true;
            } else if (opcode.equalsIgnoreCase("01000")) {
                CU.ALUSrc = true;
                res = signExtender(Integer.toBinaryString(Integer.parseInt(REG.REG[29][1], 2) - Integer.parseInt("4", 2)));
                REG.REG[29][1] = Integer.toBinaryString(Integer.parseInt(REG.REG[29][1], 2) - Integer.parseInt("4", 2));
                add = Integer.parseInt(signExtender(offset), 2) + Integer.parseInt(REG.REG[29][1], 2);
                CU.MemWrite = true;
            } else if (opcode.equalsIgnoreCase("01001")) {
                CU.ALUSrc = true;
                add = Integer.parseInt(signExtender(offset), 2) + Integer.parseInt(REG.REG[29][1], 2);
                CU.MemRead = true;
                CU.RegWrite = true;
                locRS = 29;
                res = signExtender(Integer.toBinaryString(Integer.parseInt(REG.REG[29][1], 2) + Integer.parseInt("4", 2)));
            } else if (opcode.equalsIgnoreCase("01010")) {
                CU.RegWrite = true;
                res = "00000000000000000000000000000000";
            } else if (opcode.equalsIgnoreCase("01011")) {
                int rs = Integer.parseInt(REG.REG[locRS][1]);
                int rt = Integer.parseInt(REG.REG[locRT][1]);
                if (rs < rt) {
                    CU.Jump = true;
                }
            } else if (opcode.equalsIgnoreCase("01100")) {
                int rs = Integer.parseInt(REG.REG[locRS][1]);
                int rt = Integer.parseInt(REG.REG[locRT][1]);
                if (rs == rt) {
                    CU.Jump = true;
                    CU.RegWrite = false;
                }
            } else if (opcode.equalsIgnoreCase("01101")) {
                res = REG.REG[locRS][1];
                CU.RegWrite = true;
            } else if (opcode.equalsIgnoreCase("01110")) {
                offset = ir.substring(5, 32);
                CU.Jump = true;
                System.out.println("jump ir ");
                CU.RegWrite = false;
            } else if (opcode.equalsIgnoreCase("01111")) {
                offset = ir.substring(5, 32);
                CU.ALUSrc = true;
                jumpAndLink(currMem, signExtender(offset));
            }
            busy = false;
//            System.out.println("i type: " + res);
            if (CU.RegWrite) {
                writeReg(locRT, res);
//                System.out.println(res + " result");
//                System.out.println("AAAA" + res);
            }
            if (CU.MemWrite) {
                if (!memWriteBusy) {
                    memWriteBusy = true;
                    memoryWrite(add, REG.REG[locRS][1]);
//                    System.out.println(REG.REG[locRS][1]);
                    memWriteBusy = false;
                }
            }
            if (CU.MemRead) {
                if (!memReadBusy) {
                    memReadBusy = true;
                    memoryRead(locRS, add);
                    memReadBusy = false;
                }
            }
            if (CU.Jump) {
//                System.out.println("jumping" + offset);
                jump(signExtender(offset));
            }
        }
//		System.out.println(REGISTERS.REG[locRS][1] + "LW");
    }

    private void jumpAndLink(String currentMem, String add) {
        REG.REG[30][1] = (Integer.parseInt(currentMem) + 4) + "";
        CU.Jump = true;
        jump(add);
    }

    private void jumpRegister(String add) {
        if (CU.Jump)
            jump(add);

    }

    private void jump(String add) {
        add = (Integer.parseInt(add, 2) * 4 + 1000) + "";
        System.out.println("add " + add);
        for (int i = 0; i < Memory.MEM.length; i++) {
            if (Memory.MEM[i][0].equals(add)) {
                ir = Memory.MEM[i][1];
                System.out.println("jumped to" + Integer.parseInt(add) + " ir: " + ir);
                fetch.pc = i * 4;
                System.out.println(fetch.pc + ", pc updated");
//                fetch.startFetch();
                break;
            }
        }
    }

    private void writeReg(int locRD, String res) {
//        System.out.println("PLS "+ res);
        if (CU.RegWrite) {
//            System.out.println("NO: "+signExtender(Integer.toBinaryString(Integer.parseInt(res))));
            res = signExtender(Integer.toBinaryString(Integer.parseInt(res, 2)));
//            System.out.println("PLS "+ res);
            try {
                if (locRD == 0) {
                    return;
                }
                REG.setREG(locRD, res);
                System.out.println(REG.REG[locRD][2] + " written");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(REG.REG[locRD][2]);
        }
    }

    private String not(String reg) {
        String res = "";
        for (int i = 0; i < reg.length(); i++) {
            if (reg.charAt(i) == '0') {
                res = res + '1';
            } else
                res = res + '0';
        }
        return res;
    }

    private String signExtender(String str) {
        int l = str.length();
        for (int i = 0; i < 32 - l; i++) {
            str = "0" + str;
        }
        return str;
    }

    private void memoryRead(int locRS, int add) {
        String addValue = "";
        System.out.println("d4l el read " + add);
        if (CU.MemRead) {
            System.out.println("yrb");
            if ((add - 1000) % 4 != 0) {
                int rem = (add - 1000) % 4;
                System.out.println("mod res " + rem);
                System.out.println(rem + ", " + add);
                for (int j = 0; j < Memory.MEM.length; j++) {
                    System.out.println(Integer.parseInt(Memory.MEM[j][0]) + ", " + (add - rem));
                    if (Integer.parseInt(Memory.MEM[j][0]) == (add - rem)) {
                        System.out.println("aaaaaaaaa");
                        addValue = Memory.MEM[j][1].substring(Memory.MEM[j][1].length() - 8 * (4 - rem), Memory.MEM[j][1].length());
                        addValue = addValue + Memory.MEM[j + 1][1].substring(0, 8 * rem);
                        break;
                    }
                }
            } else {
                for (int j = 0; j < Memory.MEM.length; j++) {
                    System.out.println(Memory.MEM[j][0] + ", " + add);
                    if (Integer.parseInt(Memory.MEM[j][0]) == add) {
                        System.out.println("aaa " + Memory.MEM[j][0]);
                        addValue = Memory.MEM[j][1];
                        break;
                    }
                }
            }
        }
        try {
            System.out.println(addValue + " hh");
            REG.setREG(locRS, addValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        REG.REG[locRS][1] = addValue;
    }

    private void memoryWrite(int add, String addValue) {
        if (CU.MemWrite) {
            if (add % 4 != 0) {
                int rem = add % 4;
                for (int j = 0; j < Memory.MEM.length; j++) {
                    if (Integer.parseInt(Memory.MEM[j][0]) == (add - rem)) {
                        Memory.MEM[j][1] = Memory.MEM[j][1].substring(0,
                                Memory.MEM[j][1].length() - 8 * (4 - rem) + 1)
                                + addValue.substring(0, 8 * (4 - rem) + 1);
                        Memory.MEM[j + 1][1] = addValue.substring(8 * (4 - rem), addValue.length())
                                + Memory.MEM[j + 1][1].substring(addValue.length() - 1,
                                Memory.MEM[j + 1][1].length());
                        break;
                    }
                }
            } else {
                for (int j = 0; j < Memory.MEM.length; j++) {
                    if (Integer.parseInt(Memory.MEM[j][0]) == add) {
                        Memory.MEM[j][1] = addValue;
                        break;
                    }
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
