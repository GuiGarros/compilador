package Interfaces;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class Window {
  static JFrame frame = new JFrame("Título da janela");
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

  public Container getBox(){
    return box;
  }
}
