
package principal.juegoserpientee;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import principal.juegoserpientee.Serpiente;

public class fondo extends JPanel {
    
    Color colorfondo = Color.black;
    int Tamañomaximo,tam,can,res;
    
    
    
    public fondo(int Tamañomaximo, int can){
        this.Tamañomaximo = Tamañomaximo;
        this.can = can;
        this.tam = Tamañomaximo/can;
        this.res = Tamañomaximo%can;
    }
    
    @Override
    public void paint(Graphics pintar){
        super.paint(pintar);
        pintar.setColor(colorfondo);
        for(int i = 0; i < can ; i++){
            for(int j = 0 ; j < can ; j++){
                pintar.fillRect( res/2+i*tam ,res/2+j*tam , tam-1 , tam-1);
            }
        }
        
    }
    
}
