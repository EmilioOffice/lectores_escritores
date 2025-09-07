import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PanelSimulacion extends JPanel {

    private final BaseDeDatos db = new BaseDeDatos();
    private final List<Proceso> procesos = new CopyOnWriteArrayList<>();
    private final List<Thread> hilos = new CopyOnWriteArrayList<>();
    private int proximoId = 0;

    public PanelSimulacion() {
        JButton btnAddLector = new JButton("Añadir Lector");
        btnAddLector.addActionListener(e -> agregarProceso(Proceso.Tipo.LECTOR));

        JButton btnAddEscritor = new JButton("Añadir Escritor");
        btnAddEscritor.addActionListener(e -> agregarProceso(Proceso.Tipo.ESCRITOR));

        JButton btnRemoveLector = new JButton("Eliminar Lector");
        btnRemoveLector.addActionListener(e -> removerProceso(Proceso.Tipo.LECTOR));

        JButton btnRemoveEscritor = new JButton("Eliminar Escritor");
        btnRemoveEscritor.addActionListener(e -> removerProceso(Proceso.Tipo.ESCRITOR));

        JPanel panelDeControl = new JPanel();
        panelDeControl.add(btnAddLector);
        panelDeControl.add(btnAddEscritor);
        panelDeControl.add(btnRemoveLector);
        
        panelDeControl.add(btnRemoveEscritor);

        setLayout(new BorderLayout());
        add(panelDeControl, BorderLayout.SOUTH);

        agregarProceso(Proceso.Tipo.LECTOR);
        agregarProceso(Proceso.Tipo.LECTOR);
        agregarProceso(Proceso.Tipo.ESCRITOR);
        
        Timer timer = new Timer(100, e -> {
            limpiarHilosTerminados();
            repaint();
        });
        timer.start();
    }

    private void agregarProceso(Proceso.Tipo tipo) {
        Proceso p = new Proceso(proximoId++, tipo, db);
        procesos.add(p);
        Thread t = new Thread(p, tipo.toString() + "-" + p.getId());
        hilos.add(t);
        t.start();
    }
    
    private void removerProceso(Proceso.Tipo tipo) {
        for (int i = procesos.size() - 1; i >= 0; i--) {
            Proceso p = procesos.get(i);
            if (p.getTipo() == tipo && p.getEstado() != Proceso.EstadoProceso.TERMINANDO) {
                Thread t = hilos.get(i);
                p.detener();
                t.interrupt();
                break;
            }
        }
    }

    private void limpiarHilosTerminados() {
        List<Proceso> procesosAEliminar = new ArrayList<>();
        List<Thread> hilosAEliminar = new ArrayList<>();

        for (int i = 0; i < hilos.size(); i++) {
            Thread t = hilos.get(i);
            if (!t.isAlive()) {
                procesosAEliminar.add(procesos.get(i));
                hilosAEliminar.add(t);
            }
        }

        if (!hilosAEliminar.isEmpty()) {
            procesos.removeAll(procesosAEliminar);
            hilos.removeAll(hilosAEliminar);
        }
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
        int radius = procesos.isEmpty() ? 0 : Math.min(width, height) / 2 - 100;
        int centerX = width / 2;
        int centerY = height / 2 - 50;

        for (int i = 0; i < procesos.size(); i++) {
            double angle = procesos.size() == 1 ? 0 : 2 * Math.PI * i / procesos.size();
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            
            Proceso p = procesos.get(i);
            Proceso.EstadoProceso estado = p.getEstado();

            switch (estado) {
                case INACTIVO: g2d.setColor(Color.LIGHT_GRAY); break;
                case ESPERANDO: g2d.setColor(Color.ORANGE); break;
                case ACCEDIENDO: g2d.setColor(p.getTipo() == Proceso.Tipo.LECTOR ? Color.GREEN : Color.RED); break;
                case TERMINANDO: g2d.setColor(Color.DARK_GRAY); break;
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