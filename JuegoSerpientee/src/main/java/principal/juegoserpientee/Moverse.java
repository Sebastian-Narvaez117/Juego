package principal.juegoserpientee;

import java.util.logging.Level;
import java.util.logging.Logger;
import principal.juegoserpientee.Serpiente;

public class Moverse implements Runnable {

    Serpiente serpiente;
    boolean estado = true;
    private int velocidad; // Atributo para la velocidad
    
    public Moverse(Serpiente serpiente) {
        this.serpiente = serpiente;
        this.velocidad = 70; // Velocidad inicial
    }
    
    @Override
    public void run() {
        while (estado) {
            serpiente.avanzar();
            serpiente.repaint();
            try {
                Thread.sleep(velocidad); // Usar el atributo velocidad
            } catch (InterruptedException ex) {
                Logger.getLogger(Moverse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void detener() {
        this.estado = false;
    }
    
    public void aumentarVelocidad(int incremento) {
        this.velocidad = Math.max(10, velocidad - incremento); // Asegurarse de que la velocidad no sea menor que 20 ms
    }
    
    /**
     *
     */

    
}
