package Interfaces;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Window {

  JFrame frame = new JFrame("Título da janela");
  FlowLayout flow = new FlowLayout();
  Container box = frame.getContentPane();


  public Window () {
    frame.setBounds(0, 0, 1000, 800);
    frame.setDefaultCloseOperation( WindowConstants.
            DISPOSE_ON_CLOSE);

    box.setLayout(flow);

  }
  public void setWindowStatus(Boolean opened){
    frame.setVisible(opened);
  }
  public JButton createButton(String title){
    JButton button = new JButton(title);
    box.add(button);

    return button;
  }

  public JFileChooser createFileInput(){
    JFileChooser input = new JFileChooser();
//    box.add(input);

    return input;
  }

  public JTextArea createTextInput(int width,int height, float alignX, float alignY, boolean scrollbar){
    JTextArea input = new JTextArea();
    Dimension size = new Dimension(width,height);
    input.setAlignmentX(alignX);
    input.setAlignmentY(alignY);
    input.setAutoscrolls(true);

    if(scrollbar){
      JScrollPane scroll = new JScrollPane (input,
              JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scroll.setPreferredSize(size);

      box.add(scroll);
    } else {
      input.setPreferredSize(size);
      box.add(input);
    }


    return input;
  }

  public JLabel createText(String textValue){
    JLabel text = new JLabel(textValue);
    box.add(text);

    return text;
  }

  public JLabel createText(String textValue, int width, int height){
    JLabel text = new JLabel(textValue);
    text.setAlignmentX(width);
    box.add(text);

    return text;
  }

  public JTable createTable(String[] columns, int numRows, int width,int height){
    DefaultTableModel modelo = new DefaultTableModel();
    JTable table = new JTable(modelo);
    JTableHeader tableHeader = new JTableHeader();
    Dimension size = new Dimension(width,height);

    for(String col : columns){
      modelo.addColumn(col);
    }

    table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

    modelo.setNumRows(numRows);

    JScrollPane scroll = new JScrollPane (table,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll.setPreferredSize(size);

    box.add(scroll);

    return table;
  }


  public Container getBox(){
    return box;
  }
}
