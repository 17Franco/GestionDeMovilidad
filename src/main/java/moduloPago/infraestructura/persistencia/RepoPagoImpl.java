package moduloPago.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import moduloCliente.exepciones.ClienteInvalidoException;
import moduloPago.dominio.Pago;
import moduloPago.dominio.repositorio.RepoPago;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RepoPagoImpl implements RepoPago {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Pago pago){
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser null");
        }
        em.persist(pago);


    }
    @Override

    public List<Pago> getPagosPorFecha(String ci, LocalDate fechaIni, LocalDate fechaFin){

        String sql = "select p from MPago_Pago p where p.cedulaCliente = :ci and p.fecha BETWEEN :fechaIni AND :fechaFin";


        TypedQuery<Pago> query = em.createQuery(sql, Pago.class)
                .setParameter("ci", ci)           // Antes decía "cedula"
                .setParameter("fechaIni", fechaIni) // Antes decía "inicio"
                .setParameter("fechaFin", fechaFin); // Antes decía "fin"

        return query.getResultList();
    }

    @Override
    public boolean deuda(String idCliente){
        //tiene deuda si hay un pago rechazado y no existe con mismo idCarga un pago con estado ACEPTADO
        String sql = "SELECT DISTINCT p.idCarga FROM MPago_Pago p WHERE p.cedulaCliente = :ci AND p.estado = 'RECHAZADO' AND NOT EXISTS (SELECT 1 FROM MPago_Pago p2 WHERE p2.idCarga = p.idCarga AND p2.estado = 'ACEPTADO')";

        List<Integer> deudas = em.createQuery(sql, Integer.class)
                .setParameter("ci", idCliente)
                .getResultList();

        return !deudas.isEmpty();
    }
    @Override
    public Pago obtenerDeuda(String idCliente, int idCarga) {

        String jpql = """
        SELECT p
        FROM MPago_Pago p
        WHERE p.cedulaCliente = :ci
          AND p.idCarga = :idCarga
          AND p.estado = 'RECHAZADO'
          AND NOT EXISTS (
                SELECT 1
                FROM MPago_Pago p2
                WHERE p2.idCarga = p.idCarga
                  AND p2.estado = 'ACEPTADO'
          )
        """;

        List<Pago> resultado = em.createQuery(jpql, Pago.class)
                .setParameter("ci", idCliente)
                .setParameter("idCarga", idCarga)
                .getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }
}
