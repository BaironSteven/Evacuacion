package Auxiliar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Cronometro implements Runnable, ActionListener
{
    public static int onoff = 0;
    public Cronometro()
    {
    }

    public void run(){
        Integer minutos = 0 , segundos = 0, decimas = 0;

        String min="", seg="", ds="";
        try
        {
            //Mientras cronometroActivo sea verdadero entonces seguira
            //aumentando el tiempo
            while( cronometroActivo )
            {
                Thread.sleep(1);
                
                decimas += 1;

                
                if( decimas == 10 )
                {
                    decimas = 0;
                    segundos += 1;
                    //Si los segundos llegan a 60 entonces aumenta 1 los minutos
                    //y los segundos vuelven a 0
                    if( segundos == 60 )
                    {
                        segundos = 0;
                        minutos++;
                    }
                }

                
                if( minutos < 10 ) min = "0" + minutos;
                else min = minutos.toString();
                if( segundos < 10 ) seg = "0" + segundos;
                else seg = segundos.toString();

                if( decimas < 10 ) ds = "0" + decimas;
                else ds = decimas.toString();

                //Colocamos en la etiqueta la informacion
                tiempo.setText( min + ":" + seg + ":" + ds );
            }
        }catch(Exception e){}
        
        tiempo.setText( "00:00:0" );
    }

    //Esto es para el boton iniciar y reiniciar
    public void actionPerformed( ActionEvent evt ) {
        Object o = evt.getSource();
        if( o instanceof JButton )
        {
            JButton btn = (JButton)o;
            if( btn.getText().equals("Iniciar") ){
                if(onoff == 0){
                   onoff = 1;
                   iniciarCronometro();
                }
            }
            if( btn.getText().equals("Reiniciar") ) {
                if (onoff == 1){
                   onoff = 0;
                   pararCronometro();
                }
            }
        }
    }

    //Iniciar el cronometro poniendo cronometroActivo 
    //en verdadero para que entre en el while
    public void iniciarCronometro() {
        cronometroActivo = true;
        hilo = new Thread( this );
        hilo.start();
    }

    //Esto es para parar el cronometro
    public void pararCronometro(){
        cronometroActivo = false;
    }

    public static void main(String[] args) {
        //Esta parte es para look and feel por lo tanto es opcional, solo hay que descomentarla
        //try{
        //    UIManager.setLookAndFeel( new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel() );
        //}catch( Exception e ){}
        new Cronometro();
    }

    JLabel tiempo;
    Thread hilo;
    boolean cronometroActivo;
}