package Memory;

import Exceptions.FinalRegException;

public class Registers {
    public int PRINT = 0;
    public String[] REG[] = {
            {"00000", "00000000000000000000000000000000", "ZERO"},
            {"00001", "00000000000000000000000000000001", "ONE"},
            {"00010", "00000000000000000000000000000000", "ST"},
            {"00011", "00000000000000000000000000000000", "O0"},
            {"00100", "00000000000000000000000000000000", "O1"},
            {"00101", "00000000000000000000000000000000", "O2"},
            {"00110", "00000000000000000000000000000000", "P0"},
            {"00111", "00000000000000000000000000000000", "P1"},
            {"01000", "00000000000000000000000000000000", "P2"},
            {"01001", "00000000000000000000000000000000", "P3"},
            {"01010", "00000000000000000000000000000000", "T0"},
            {"01011", "00000000000000000000000000000000", "T1"},
            {"01100", "00000000000000000000000000000000", "T2"},
            {"01101", "00000000000000000000000000000000", "T3"},
            {"01110", "00000000000000000000000000000000", "T4"},
            {"01111", "00000000000000000000000000000000", "T5"},
            {"10000", "00000000000000000000000000000000", "T6"},
            {"10001", "00000000000000000000000000000000", "T7"},
            {"10010", "00000000000000000000000000000000", "S0"},
            {"10011", "00000000000000000000000000000000", "S1"},
            {"10100", "00000000000000000000000000000000", "S2"},
            {"10101", "00000000000000000000000000000000", "S3"},
            {"10110", "00000000000000000000000000000000", "S4"},
            {"10111", "00000000000000000000000000000000", "S5"},
            {"11000", "00000000000000000000000000000000", "S6"},
            {"11001", "00000000000000000000000000000000", "K0"},
            {"11010", "00000000000000000000000000000000", "K1"},
            {"11011", "00000000000000000000000000000000", "GP"},
            {"11100", "00000000000000000000010010101100", "SP"},
            {"11101", "00000000000000000000000000000000", "FP"},
            {"11110", "00000000000000000000000000000000", "RA"},
            {"11111", "00000000000000000000000000000000", "HP"}
    };

    public int getPRINT(){
        return PRINT;
    }

    public void setPRINT(int res){
        PRINT = res;
    }

    public void setREG(int add, String value) throws FinalRegException{
        if(add == 0 || add == 1){
            System.err.println(add);
            throw new FinalRegException(REG[add], "Cannot change this register value");
        }
        else{
            REG[add][1] = value;
        }
    }
    public String getREGValue(int add){
        return REG[add][1];
    }
}
