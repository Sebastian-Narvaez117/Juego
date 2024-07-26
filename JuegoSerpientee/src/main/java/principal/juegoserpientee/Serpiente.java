package principal.juegoserpientee;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Serpiente extends JPanel {
    
    Color colorserpiente = Color.green;
    Color colormanzana = Color.red;
    int Tamañomaximo, tam, can, res;
    List<int[]> serpiente = new ArrayList<>();
    int[] manzana = new int[2];
    String direccion = "de";
    String siguientedireccion = "de";
    
    int puntaje = 0; // Variable para almacenar el puntaje
    
    Thread hilo;
    Moverse muevete;
    
    public Serpiente(int Tamañomaximo, int can) {
        this.Tamañomaximo = Tamañomaximo;
        this.can = can;
        this.tam = Tamañomaximo / can;
        this.res = Tamañomaximo % can;
        int[] a = {can / 2 - 1, can / 2 - 1};
        int[] b = {can / 2, can / 2 - 1};
        serpiente.add(a);
        serpiente.add(b);
        agregarmanzana();
        
        muevete = new Moverse(this);
        hilo = new Thread(muevete);
        hilo.start();
    }
    
    @Override
    public void paint(Graphics pintar) {
        super.paint(pintar);
        pintar.setColor(colorserpiente);
        
        for (int[] par : serpiente) {
            pintar.fillRect(res / 2 + par[0] * tam, res / 2 + par[1] * tam, tam - 1, tam - 1);
        }
        
        pintar.setColor(colormanzana);
        pintar.fillRect(res / 2 + manzana[0] * tam, res / 2 + manzana[1] * tam, tam - 1, tam - 1);
        
        pintar.setColor(Color.black);
        pintar.setFont(new Font("Arial", Font.BOLD, 10));
        pintar.drawString("Puntaje: " + puntaje, 10, 8); // Dibujar el puntaje en una posición más arriba
    }
    
    public void avanzar() {
        igualardireccion();
        int[] cola = serpiente.get(serpiente.size() - 1);
        int agregarx = 0;
        int agregary = 0;
        switch (direccion) {
            case "de":
                agregarx = 1;
                break;
            case "iz":
                agregarx = -1;
                break;
            case "ar":
                agregary = -1;
                break;
            case "ab":
                agregary = 1;
                break;
        }
        
        int[] cabeza = {Math.floorMod(cola[0] + agregarx, can), Math.floorMod(cola[1] + agregary, can)};
        boolean existe = false;
        
        for (int i = 0; i < serpiente.size(); i++) {
            if (cabeza[0] == serpiente.get(i)[0] && cabeza[1] == serpiente.get(i)[1]) {
                existe = true;
                break;
            }
        }
        if (existe) {
            JOptionPane.showMessageDialog(this, "PERDISTE");
            muevete.detener(); // Detener el juego si la serpiente choca consigo misma
        } else {
            if (cabeza[0] == manzana[0] && cabeza[1] == manzana[1]) {
                serpiente.add(cabeza);
                puntaje += 10; // Incrementar el puntaje en 10
                agregarmanzana();
                muevete.aumentarVelocidad(1); // Aumentar la velocidad en 1 ms
            } else {
                serpiente.add(cabeza);
                serpiente.remove(0);
            }
        }
    }
    
    public void agregarmanzana() {
        boolean existe = false;
        int a = (int) (Math.random() * can);
        int b = (int) (Math.random() * can);
        for (int[] par : serpiente) {
            if (par[0] == a && par[1] == b) {
                existe = true;
                agregarmanzana();
                break;
            }
        }
        if (!existe) {
            this.manzana[0] = a;
            this.manzana[1] = b;
        }
    }
    
    public void cambiardireccion(String dir) {
        if ((this.direccion.equals("de") || this.direccion.equals("iz")) && (dir.equals("ar") || dir.equals("ab"))) {
            this.siguientedireccion = dir;
        }
        if ((this.direccion.equals("ar") || this.direccion.equals("ab")) && (dir.equals("iz") || dir.equals("de"))) {
            this.siguientedireccion = dir;
        }
    }
    
    public void igualardireccion() {
        this.direccion = siguientedireccion;
    }
    
    
}
