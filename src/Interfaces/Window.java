package Interfaces;

import Services.SimpleHeaderRenderer;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Window {

    JFrame frame = new JFrame("Título da janela");
    FlowLayout flow = new FlowLayout();
    Container box = frame.getContentPane();

    public Window() {
        frame.setBounds(0, 0, 1000, 800);
        frame.setDefaultCloseOperation(WindowConstants.
                DISPOSE_ON_CLOSE);
        box.setLayout(flow);

    }

    public void setWindowStatus(Boolean opened) {
        frame.setVisible(opened);
    }

    public JButton createButton(String title) {
        JButton button = new JButton(title);
        return button;
    }

    public JFileChooser createFileInput() {
        JFileChooser input = new JFileChooser();
        return input;
    }

    public JTextArea createTextInput(int width, int height, float alignX, float alignY) {
        JTextArea input = new JTextArea();
        Dimension size = new Dimension(width, height);
        input.setAlignmentX(alignX);
        input.setAlignmentY(alignY);
        input.setAutoscrolls(true);
        input.setPreferredSize(size);
        return input;
    }

    public JLabel createText(String textValue) {
        JLabel text = new JLabel(textValue);
        return text;
    }

    public JScrollPane createScroll(int width, int height, Component c) {
        Dimension size = new Dimension(width, height);
        JScrollPane scroll = new JScrollPane(c,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(size);
        return scroll;
    }

    public JTable createTable(String[] columns, int numRows) {
        DefaultTableModel modelo = new DefaultTableModel();
        JTable table = new JTable(modelo);
        JTableHeader tableHeader = new JTableHeader();
        for (String col : columns) {
            modelo.addColumn(col);
        }
        table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
        return table;
    }

    public JPanel createPanel(int width, int height) {
        JPanel panel = new JPanel();
        Dimension size = new Dimension(width, height);
        panel.setPreferredSize(size);

        return panel;
    }

    public Container getBox() {
        return box;
    }
}
