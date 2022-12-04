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

  public boolean findVariable(String token)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(token) && stack.get(i).type.equals("variável"))
      {
        return true;
      }
    }
    return false;
  }

  public boolean findDuplicatedVariable(String token,int level)
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(token) && stack.get(i).type.equals("variável") && stack.get(i).level == level)
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

  public int findFunction(String value) //0 não nada, 1 achou o identificador, 2 é função e é tipo booleano ou inteiro
  {
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value) && !stack.get(i).type.equals("procedimento"))
      {
        if(stack.get(i).type.equals("funcao"))
        {
          return 2;
        }
        return 1;
      }
    }
    return 0;
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

  public int getPosicaoMemoriaVariavel(String[] value){
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value[0]))
      {
        return stack.get(i).p_posicao;
      }
    }
    return -1;
  }

  public int getPosicaoMemoriaFuncao(String[] value){
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("funcao")))
      {
        return stack.get(i).rot;
      }
    }
    return -1;
  }

  public int getPosicaoMemoriaProcedimento(String[] value){
    System.out.println(value[0]);
    System.out.println(value[1]);
    for(int i = 0; i < stack.size(); i++)
    {
      if(stack.get(i).lexema.equals(value[0]) && stack.get(i).type.equals("procedimento"))
      {
        return stack.get(i).rot;
      }
    }
    return -1;
  }

}
