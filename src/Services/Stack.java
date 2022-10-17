package Services;

import java.util.LinkedList;

public class Stack {
  public LinkedList<String> stack = new LinkedList<String>();

  public String pop() {
    return stack.removeFirst();
  }

  public void push(String value) {
    stack.addFirst(value);
  }
}
