package stateless;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Localidad;

@Stateless
public class LocalidadServiceBean implements LocalidadService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Localidad find(int id) {
        return getEntityManager().find(Localidad.class, id);
    }

    @Override
    public Collection<Localidad> findAll() {
        return em.createQuery("select l from Localidad l", Localidad.class).getResultList();
    }

    @Override
    public ResultsPage<Localidad> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Localidad.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Localidad.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Localidad> resultPage = new ResultsPage<Localidad>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public Localidad findByNombre(String nombre) {
        try {
            return em.createQuery("select l from Localidad l where l.nombre = :nombre", Localidad.class)
                    .setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Localidad update(Localidad localidad) {
        em.merge(localidad);
        return localidad;
    }

    @Override
    public Localidad remove(int id) {
        Localidad localidad = em.find(Localidad.class, id);
        if (localidad != null)
            em.remove(localidad);

        return localidad;
    }

    public Localidad create(Localidad localidad) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        em.persist(localidad); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return localidad;
    }

    @Override
    public Collection<Localidad> search(String nombre) {
        try {
            return em.createQuery("select l from Localidad l where UPPER(l.nombre) LIKE CONCAT('%',UPPER(:nombre),'%')",
                    Localidad.class).setParameter("nombre", nombre).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}