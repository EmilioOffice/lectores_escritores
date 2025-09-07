import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelSimulacion extends JPanel {

    private final BaseDeDatos db = new BaseDeDatos();
    private final List<Proceso> procesos = new ArrayList<>();
    private final List<Thread> hilos = new ArrayList<>();
    private int proximoId = 0;

    public PanelSimulacion() {
        JButton btnAddLector = new JButton("Añadir Lector");
        btnAddLector.addActionListener(e -> agregarProceso(Proceso.Tipo.LECTOR));

        JButton btnAddEscritor = new JButton("Añadir Escritor");
        btnAddEscritor.addActionListener(e -> agregarProceso(Proceso.Tipo.ESCRITOR));

        JPanel panelDeControl = new JPanel();
        panelDeControl.add(btnAddLector);
        panelDeControl.add(btnAddEscritor);

        setLayout(new BorderLayout());
        add(panelDeControl, BorderLayout.SOUTH);

        agregarProceso(Proceso.Tipo.LECTOR);
        agregarProceso(Proceso.Tipo.LECTOR);
        agregarProceso(Proceso.Tipo.ESCRITOR);
        
        Timer timer = new Timer(100, e -> repaint());
        timer.start();
    }

    private void agregarProceso(Proceso.Tipo tipo) {
        Proceso p = new Proceso(proximoId++, tipo, db);
        procesos.add(p);
        Thread t = new Thread(p);
        hilos.add(t);
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        dibujarBaseDeDatos(g2d, width / 2, height / 2 - 50);

        dibujarProcesos(g2d, width, height);
    }

    private void dibujarBaseDeDatos(Graphics2D g2d, int x, int y) {
        BaseDeDatos.EstadoDB estadoDB = db.getEstado();
        String textoEstado = "";
        
        switch (estadoDB) {
            case INACTIVA:
                g2d.setColor(Color.GRAY);
                textoEstado = "INACTIVA";
                break;
            case LECTURA:
                g2d.setColor(Color.CYAN);
                textoEstado = "LECTURA (Lectores: " + db.getLectoresActivos().toString() + ")";
                break;
            case ESCRITURA:
                g2d.setColor(Color.MAGENTA);
                textoEstado = "ESCRITURA (Escritor: " + db.getEscritorActivo() + ")";
                break;
        }
        
        g2d.fillRect(x - 100, y - 50, 200, 100);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("BASE DE DATOS", x - 70, y);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(textoEstado, x - 90, y + 20);
    }
    
    private void dibujarProcesos(Graphics2D g2d, int width, int height) {
        int radius = Math.min(width, height) / 2 - 100;
        int centerX = width / 2;
        int centerY = height / 2 - 50;

        for (int i = 0; i < procesos.size(); i++) {
            double angle = 2 * Math.PI * i / procesos.size();
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            
            Proceso p = procesos.get(i);
            Proceso.EstadoProceso estado = p.getEstado();

            switch (estado) {
                case INACTIVO: g2d.setColor(Color.LIGHT_GRAY); break;
                case ESPERANDO: g2d.setColor(Color.ORANGE); break;
                case ACCEDIENDO: g2d.setColor(p.getTipo() == Proceso.Tipo.LECTOR ? Color.GREEN : Color.RED); break;
            }

            g2d.fillOval(x - 20, y - 20, 40, 40);
            g2d.setColor(Color.BLACK);
            String tipoStr = p.getTipo() == Proceso.Tipo.LECTOR ? "L" : "E";
            g2d.drawString(tipoStr + p.getId(), x - 7, y + 5);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString(estado.toString(), x - 30, y + 35);
        }
    }
}