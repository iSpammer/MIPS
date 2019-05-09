package GUI;

import Assembly.Fetch;
import Lexer.Lexer;
import Memory.Memory;

import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import Memory.Registers;

public class GUI extends JFrame implements ActionListener {
    TextArea area;
    JButton button;
    JTextArea txt1;

    public GUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 600, 600);
        setLayout(null);
        this.setVisible(true);
        area = new TextArea();
        button = new JButton("send");
        txt1 = new JTextArea();

        area.setBounds(10, 420, 450, 130);
        txt1.setBounds(5, 10,574 , 400);
        button.setBounds(480, 500, 100, 40);
        area.setEditable(false);
        add(area);
        add(button);
        add(txt1);
        button.addActionListener(this);
        this.validate();
        this.repaint();
    }


    public static void main(String[] args) {
        new GUI();
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        String textFieldValue = txt1.getText();
        Registers reg = new Registers();
        Fetch fetch = new Fetch(reg);
        String[] cmd = textFieldValue.split("\n");
        Lexer lex = new Lexer(reg);

        for(int i = 0; i < cmd.length; i++){
            Memory.insertToMEM(lex.lex(cmd[i]));
        }

        System.out.println(textFieldValue);
        fetch.startFetch();

        String registerValue = "";
        for(int i = 0 ; i < reg.REG.length; i++){
            for(int j = 0; j < 3; j ++){
                registerValue = registerValue +" "+ reg.REG[i][j];
            }
            registerValue = registerValue+"\n";
        }
        area.setText("");
        area.append(Arrays.deepToString(fetch.REG.REG));
    }
}