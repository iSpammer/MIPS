package Lexer;

import Memory.Registers;


public class Lexer {
    private Registers REG;

    public Lexer(Registers REG) {
        this.REG = REG;
//		this.lex = lex;
    }

    public String lex(String ir) {
        String[] lex = ir.split(" ");
        for (int i = 0; i < lex.length; i++) {
            lex[i] = lex[i].trim();
        }
//        System.out.println(Arrays.toString(lex));
        if (!(lex[0].equals("add") || lex[0].equals("sub") || lex[0].equals("div") || lex[0].equals("and")
                || lex[0].equals("or") || lex[0].equals("sll") || lex[0].equals("slr") || lex[0].equals("jr"))) {
//            System.out.println(lex[1]);
            String rt = fetchRt(lex[1]);
            String rs = "";
            String imm = "";
            if (!lex[0].contains("j") && !lex[0].equals("jr")) {
                rs = fetchRs(lex[2]);
                imm = Integer.toBinaryString(Integer.parseInt(lex[3]));
            }


            int l = imm.length();
            for (int i = 0; i < 17 - l; i++) {
                imm = '0' + imm;
            }
//            System.out.println(imm);
            String opcode = "";
            if (lex[0].contains("addi")) {
                opcode = "00001";
            } else if (lex[0].contains("ori")) {
                opcode = "00010";
            } else if (lex[0].contains("xori")) {
                opcode = "00011";
            } else if (lex[0].contains("andi")) {
                opcode = "00100";
            } else if (lex[0].contains("lw")) {
                opcode = "00101";
            } else if (lex[0].contains("swap")) {
                opcode = "00110";
            } else if (lex[0].contains("sw")) {
                opcode = "00111";
            } else if (lex[0].contains("push")) {
                opcode = "01000";
            } else if (lex[0].contains("pop")) {
                opcode = "01001";
            } else if (lex[0].contains("delete")) {
                opcode = "01010";
            } else if (lex[0].contains("blt")) {
                opcode = "01011";
            } else if (lex[0].contains("beq")) {
                opcode = "01100";
            } else if (lex[0].contains("mov")) {
                opcode = "01101";
            } else if (lex[0].contains("j")) {
                rt = "";
                imm = signExtender(Integer.toBinaryString(Integer.parseInt(lex[1])));
                opcode = "01110";
            } else if (lex[0].contains("jal")) {
                opcode = "01111";
                rt = "";
                imm = signExtender(Integer.toBinaryString(Integer.parseInt(lex[1])));
            }
            ir = opcode + rt + rs + imm;
//            System.out.println(ir + " lexed");
        } else {
            String opcode = "00000";

            String rd = fetchRd(lex[1]);


            String rs = fetchRs(lex[2]);

            String shamt = "00000000";

            if (lex[0].contains("add")) {
                String rt = fetchRt(lex[3]);
                String func = "0000";
                ir = opcode + rs + rt + rd + shamt + func;
            } else if (lex[0].contains("sub")) {
                String func = "0001";
                String rt = fetchRt(lex[3]);
                ir = opcode + rs + rt + rd + shamt + func;
            } else if (lex[0].contains("div")) {
                String func = "0010";
                String rt = fetchRt(lex[3]);
                ir = opcode + rs + rt + rd + shamt + func;
                // String
            } else if (lex[0].contains("and")) {
                String func = "0011";
                String rt = fetchRt(lex[3]);
                ir = opcode + rs + rt + rd + shamt + func;
                // String
            } else if (lex[0].contains("or")) {
                String func = "0100";
                String rt = fetchRt(lex[3]);
                ir = opcode + rs + rt +rd+ shamt + func;
            } else if (lex[0].contains("sll")) {
                String func = "0101";
                String rt = "00000";
                shamt = signExtender8(Integer.toBinaryString(Integer.parseInt(fetchRd(lex[3]))));
                ir = opcode + rs +rt+ rd + shamt + func;
            } else if (lex[0].contains("slr")) {
                String func = "0110";
                shamt = signExtender8(Integer.toBinaryString(Integer.parseInt(fetchRd(lex[3]))));
                String rt = "00000";
                ir = opcode + rs + rt + rd + shamt + func;
            } else if (lex[0].contains("jr")) {
                String func = "0111";
                ir = opcode + rs + shamt + "000000000" + func;
            }
        }
        return ir;
    }

    private String fetchRd(String rd) {
        rd = loopReg(rd);
        System.out.println(rd+" taaaaaaaaaa dredndn taaaaaaaaaa dredndn dredndn dredndndnredndndndndaaaaaaaa");
        return rd;
    }

    private String loopReg(String rd) {
        for (int i = 0; i < REG.REG.length; i++) {
            if (rd.equals(REG.REG[i][2]))
                rd = REG.REG[i][0];
        }
        return rd;
    }

    private String fetchRt(String rt) {
        rt = loopReg(rt);
        return rt;
    }

    private String fetchRs(String rs) {
        rs = loopReg(rs);
        System.out.println("rss "+rs);
        return rs;
    }

    private String signExtender(String str) {
        int l = str.length();
        for (int i = 0; i < 27 - l; i++) {
            str = '0'+str;
        }
        return str;
    }
    private String signExtender8(String str) {
        int l = str.length();
        for (int i = 0; i < 8 - l; i++) {
            str = '0'+str;
        }
        return str;
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer(new Registers());
        // 00000101001010110110000000000000
        String asd = (lexer.lex("j 2"));
        System.out.println(asd);
        String aa = (lexer.lex("sll S0 S0 4"));
        System.out.println(aa);
        String aaa = (lexer.lex("addi S0 S0 3"));
        System.out.println(aaa);
        String aaaasd = (lexer.lex("addi S0 S0 2"));
        System.out.println(aaaasd);
//        String assa = (lexer.lex("addi S6 S5 0"));
//        System.out.println(assa);
//        String axxaa = (lexer.lex("addi S6 S6 4"));
//        System.out.println(axxaa);
    }
}
