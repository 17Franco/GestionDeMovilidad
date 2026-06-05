package infraestructura.inicio;


import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import moduloCliente.dominio.Grupo;

@Startup
@Singleton
//al levantar se crean los grupos
//despues se ve otra forma
public class DataInitializer {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {

        if (em.find(Grupo.class, "appMovil") == null) {
            Grupo g1 = new Grupo();
            g1.setNombre("appMovil");
            em.persist(g1);
        }
        if(em.find(Grupo.class, "gestorWeb") == null){
            Grupo g2 = new Grupo();
            g2.setNombre("gestorWeb");
            em.persist(g2);
        }
        if(em.find(Grupo.class, "admin") == null){
            Grupo g3 = new Grupo();
            g3.setNombre("admin");
            em.persist(g3);
        }

    }
}