package Services;

import java.util.LinkedList;

public class Stack { //simbol table
  public LinkedList<SimbolTable> stack = new LinkedList<SimbolTable>();


  public SimbolTable pop() {
    return stack.removeFirst();
  }

  public void push(SimbolTable value) {
    stack.addFirst(value);
  }

  public boolean find(SimbolTable value)
  {
    if(stack.stream().findAny().isEmpty())
    {
      return false; //not exists
    }
    else
    {
      return true; //exists
    }


  }
}
