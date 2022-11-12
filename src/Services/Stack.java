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

    if(stack.indexOf(value) == -1)
    {
      return false; //not exists
    }

    return true;
  }

  public boolean findVariable(String token,int level)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(token) && !stack.get(i).type.equals("variável") && stack.get(i).level == level)
      {
        return true;
      }
    }

    return false;
  }
  public boolean findProcedure(String value)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value) && stack.get(i).type.equals("procedimento"))
      {
        return true;
      }
    }

    return false;
  }

  public boolean findFunction(String value)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value) && (stack.get(i).type.equals("funcaointeiro") || stack.get(i).type.equals("funcaobooleano")))
      {
        return true;
      }
    }

    return false;
  }
  public boolean findIdentifier(String value)
  {

    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value) && !stack.get(i).type.equals("variável"))
      {
        return true;
      }
    }
    return false;
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
