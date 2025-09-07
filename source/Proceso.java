import java.util.Random;

public class Proceso implements Runnable {

    public enum Tipo { LECTOR, ESCRITOR }
    public enum EstadoProceso { INACTIVO, ESPERANDO, ACCEDIENDO, TERMINANDO }

    private final int id;
    private final Tipo tipo;
    private final BaseDeDatos db;
    private volatile boolean corriendo = true;
    private final Random random = new Random();
    private volatile EstadoProceso estado;

    public Proceso(int id, Tipo tipo, BaseDeDatos db) {
        this.id = id;
        this.tipo = tipo;
        this.db = db;
        this.estado = EstadoProceso.INACTIVO;
    }

    @Override
    public void run() {
        while (corriendo) {
            try {
                setEstado(EstadoProceso.INACTIVO);
                Thread.sleep(random.nextInt(5000) + 3000);

                if (!corriendo) break;

                setEstado(EstadoProceso.ESPERANDO);
                if (tipo == Tipo.LECTOR) {
                    db.iniciarLectura(id);
                } else {
                    db.iniciarEscritura(id);
                }

                if (!corriendo) {
                    if (tipo == Tipo.LECTOR) db.finalizarLectura(id);
                    else db.finalizarEscritura();
                    break;
                }

                setEstado(EstadoProceso.ACCEDIENDO);
                Thread.sleep(random.nextInt(4000) + 2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                corriendo = false;
            } finally {
                if (estado == EstadoProceso.ACCEDIENDO) {
                    if (tipo == Tipo.LECTOR) {
                        db.finalizarLectura(id);
                    } else {
                        db.finalizarEscritura();
                    }
                }
            }
        }
    }

    public void detener() {
        this.estado = EstadoProceso.TERMINANDO;
        this.corriendo = false;
    }

    public int getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    private synchronized void setEstado(EstadoProceso estado) {
        if (this.estado != EstadoProceso.TERMINANDO) {
            this.estado = estado;
        }
    }
}