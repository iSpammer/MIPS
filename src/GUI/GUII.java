/*
 * Created by JFormDesigner on Sat Apr 27 10:20:32 EET 2019
 */

package GUI;

import Assembly.Fetch;
import Lexer.Lexer;
import Memory.Memory;
import Memory.Registers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;

/**
 * @author Ossama Akram
 */
public class GUII extends JFrame implements ActionListener {
    private Fetch fetch;
    private Lexer lex;
    private Registers reg;

    public GUII() {
        initComponents();
    }

    public static void main(String[] args) {

        new GUII();
        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.gray));
        uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.GRAY));
        JFrame.setDefaultLookAndFeelDecorated(true);

    }

    private void initComponents() {
        reg = new Registers();
        lex = new Lexer(reg);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ossama Akram
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        button1 = new JButton();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        scrollPane3 = new JScrollPane();
        textArea3 = new JTextArea();
        button1.addActionListener(this);
        this.setResizable(false);
        //======== this ========
        setForeground(Color.darkGray);
        setBackground(Color.darkGray);
        setIconImage(new ImageIcon("C:\\Users\\Lenovo\\Downloads\\Documents\\mips.png").getImage());
        setTitle("MIPS ASM");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {
            scrollPane1.setBackground(Color.gray);

            //---- textArea1 ----
            textArea1.setForeground(SystemColor.text);
            textArea1.setBackground(Color.darkGray);
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 0, 375, 380);

        //---- button1 ----
        button1.setText("RUN!");
        button1.setForeground(SystemColor.controlShadow);
        button1.setBackground(Color.darkGray);
        contentPane.add(button1);
        button1.setBounds(0, 385, 375, 30);

        //======== scrollPane2 ========
        {
            scrollPane2.setBackground(Color.gray);

            //---- textArea2 ----
            textArea2.setEditable(false);
            textArea2.setForeground(SystemColor.text);
            textArea2.setBackground(Color.darkGray);
            scrollPane2.setViewportView(textArea2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(375, 0, 295, 420);

        //======== scrollPane3 ========
        {
            scrollPane3.setBackground(Color.gray);

            //---- textArea3 ----
            textArea3.setEditable(false);
            textArea3.setForeground(SystemColor.text);
            textArea3.setBackground(Color.darkGray);
            scrollPane3.setViewportView(textArea3);
        }
        contentPane.add(scrollPane3);
        scrollPane3.setBounds(670, 0, 270, 420);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        for (int i = 0; i < reg.REG.length; i++) {
            textArea2.append(Arrays.toString(reg.REG[i]) + "\n");
        }
        for (int i = 0; i < Memory.MEM.length; i++) {
            textArea3.append(Arrays.toString(Memory.MEM[i]) + "\n");
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ossama Akram
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button1;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JScrollPane scrollPane3;
    private JTextArea textArea3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == button1 && !textArea1.getText().isEmpty()) {
            // TODO Auto-generated method stub
            String textFieldValue = textArea1.getText();
//            Fetch fetch = new Fetch(reg);
            fetch = new Fetch(reg);

            Memory.idx = 0;
            String[] cmds = textFieldValue.split("\n");
            for (String cmd : cmds) {
                System.out.println("inserted " + cmd);
                System.out.println("lexed " + lex.lex(cmd));
                Memory.insertToMEM(lex.lex(cmd));
            }

            JOptionPane.showMessageDialog( null, "Executing \n"+textFieldValue );

//            System.out.println(textFieldValue);
            fetch.startFetch();
            textArea2.setText("");
            textArea3.setText("");
            for (int i = 0; i < reg.REG.length; i++) {
                textArea2.append(Arrays.toString(reg.REG[i]) + "\n");
            }
            for (int i = 0; i < Memory.MEM.length; i++) {
                textArea3.append(Arrays.toString(Memory.MEM[i]) + "\n");
            }
            Memory.idx = 0;
            for (int i = 0; i < Memory.MEM.length && !Memory.MEM[i][1].equals("00000000000000000000000000000000"); i++) {
                Memory.insertToMEM("00000000000000000000000000000000");
            }
//            textArea1.setText("");
            reg = new Registers();

        }
    }
}
