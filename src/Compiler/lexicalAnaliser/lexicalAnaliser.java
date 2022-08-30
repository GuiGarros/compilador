package lexicalAnaliser;

public class lexicalAnaliser{

    String codigo;

    /**
     * @author Guilherme Garros
     * @since 25/08/2022
     * @version 1
     * @parms @array txt transformado em array
     * @copyright Grupo 1*/
    public static String tratarEspaco(String texto)
    {
        try {
                texto = texto.replace(" ","");

                return texto;

        }catch (Exception e)
        {
            throw e;
        }
    }

    public static void removeComentario(String texto)
    {
        try{
            boolean aux = false;

            for(int i = 0; i < texto.length();i++)
            {
                if(texto.charAt(i) == '{') aux = true;
                if(texto.charAt(i) == '}')
                {
                    texto = texto.substring(0,i)+texto.substring((i+1));
                    aux = false;
                }

                if(aux)
                {
                    texto = texto.substring(0,i)+texto.substring((i+1));
                    i--;
                }

            }
            System.out.println(texto);
        }catch (Exception e)
        {
            throw e;
        }
    }
}