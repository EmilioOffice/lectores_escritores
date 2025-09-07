import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.List;

public class BaseDeDatos {
    
    public enum EstadoDB { INACTIVA, LECTURA, ESCRITURA }

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    
    private final List<Integer> lectoresActivos = new CopyOnWriteArrayList<>();
    private int escritorActivo = -1;

    public void iniciarLectura(int idLector) {
        readLock.lock();
        lectoresActivos.add(idLector);
    }

    public void finalizarLectura(int idLector) {
        lectoresActivos.remove(Integer.valueOf(idLector));
        readLock.unlock();
    }

    public void iniciarEscritura(int idEscritor) {
        writeLock.lock();
        escritorActivo = idEscritor;
    }

    public void finalizarEscritura() {
        escritorActivo = -1;
        writeLock.unlock();
    }

    // --- Métodos para Monitoreo ---
    public EstadoDB getEstado() {
        if (escritorActivo != -1) {
            return EstadoDB.ESCRITURA;
        }
        if (!lectoresActivos.isEmpty()) {
            return EstadoDB.LECTURA;
        }
        return EstadoDB.INACTIVA;
    }

    public List<Integer> getLectoresActivos() {
        return lectoresActivos;
    }

    public int getEscritorActivo() {
        return escritorActivo;
    }
}