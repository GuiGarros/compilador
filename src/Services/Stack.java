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
  {/

    if(stack.indexOf(value) == -1)
    {
      return false; //not exists
    }

    return true;
  }

  public boolean getIdentificador(SimbolTable value)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value.lexema) && !stack.get(i).type.equals("variável"))
      {
        return true;
      }
    }
    return false;
  }
}
