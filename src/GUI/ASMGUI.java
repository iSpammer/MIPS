/*
 * Created by JFormDesigner on Fri Apr 26 16:19:08 EET 2019
 */

package GUI;

import Assembly.Fetch;
import Lexer.Lexer;
import Memory.Memory;
import Memory.Registers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 * @author unknown
 */
public class ASMGUI extends JFrame implements ActionListener {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ossama Akram
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane3;

    private JTextArea textArea1;
    private JTextArea textField1;
    private JButton button1;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public ASMGUI() {
        initComponents();
    }

    public static void main(String[] args) {
        new ASMGUI();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ossama Akram
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        scrollPane1 = new JScrollPane();
        scrollPane3 = new JScrollPane();

        textArea1 = new JTextArea();

        textField1 = new JTextArea();
        button1 = new JButton();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 0, 275, 435);
        textArea2.setEditable(false);

        //---- button1 ----
        button1.setText("RUN");
        contentPane.add(button1);
        button1.setBounds(275, 408, 305, button1.getPreferredSize().height);
        button1.addActionListener(this);
        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(textArea2);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(275, 0, 305, 405);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
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
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == button1 && !textArea1.getText().isEmpty()) {
            // TODO Auto-generated method stub
            String textFieldValue = textArea1.getText();
            Registers reg = new Registers();
            Fetch fetch = new Fetch(reg);
            String[] cmd = textFieldValue.split("\n");
            Lexer lex = new Lexer(reg);

            for (int i = 0; i < cmd.length; i++) {
                Memory.insertToMEM(lex.lex(cmd[i]));
            }

//            System.out.println(textFieldValue);
            JOptionPane.showMessageDialog( null, "Executing \n"+textFieldValue );


            fetch.startFetch();

            String registerValue = "";

            textArea2.setText("");

            for (int i = 0; i < reg.REG.length; i++) {
                for (int j = 0; j < 3; j++) {
                    registerValue = registerValue + " " + reg.REG[i][j];
                }
                textArea2.append(Arrays.toString(reg.REG[i]) + "\n");

                registerValue = registerValue + "\n";
            }
            textArea1.setText("");
            textField1.setText("");
        }
    }
}
