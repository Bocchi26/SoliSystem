package games.solisystem.application.dto;

public class ReporteResponse {
    private final long total;
    private final long creadas;
    private final long enRevision;
    private final long aprobadas;
    private final long rechazadas;
    private final long cerradas;

    public ReporteResponse(long total, long creadas, long enRevision, long aprobadas, long rechazadas, long cerradas) {
        this.total = total;
        this.creadas = creadas;
        this.enRevision = enRevision;
        this.aprobadas = aprobadas;
        this.rechazadas = rechazadas;
        this.cerradas = cerradas;
    }

    public long getTotal() {
        return total;
    }

    public long getCreadas() {
        return creadas;
    }

    public long getEnRevision() {
        return enRevision;
    }

    public long getAprobadas() {
        return aprobadas;
    }

    public long getRechazadas() {
        return rechazadas;
    }

    public long getCerradas() {
        return cerradas;
    }

    @Override
    public String toString() {
        return "=== Reporte de Solicitudes ===\n" +
                "Total:       " + total + "\n" +
                "Creadas:     " + creadas + "\n" +
                "En Revisión: " + enRevision + "\n" +
                "Aprobadas:   " + aprobadas + "\n" +
                "Rechazadas:  " + rechazadas + "\n" +
                "Cerradas:    " + cerradas;
    }
}
