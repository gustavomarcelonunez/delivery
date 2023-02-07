package stateless;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Provincia;

@Stateless
public class ProvinciaServiceBean implements ProvinciaService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Provincia find(int id) {
        return getEntityManager().find(Provincia.class, id);
    }

    @Override
    public Collection<Provincia> findAll() {
        return em.createQuery("select l from Provincia l", Provincia.class).getResultList();
    }

    @Override
    public ResultsPage<Provincia> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Provincia.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Provincia.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Provincia> resultPage = new ResultsPage<Provincia>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public Provincia findByNombre(String nombre) {
        try {
            return em.createQuery("select l from Provincia l where l.nombre = :nombre", Provincia.class)
                    .setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Provincia update(Provincia provincia) {
        em.merge(provincia);
        return provincia;
    }

    @Override
    public Provincia remove(int id) {
        Provincia provincia = em.find(Provincia.class, id);
        if (provincia != null)
            em.remove(provincia);

        return provincia;
    }

    public Provincia create(Provincia provincia) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        em.persist(provincia); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return provincia;
    }

    @Override
    public Collection<Provincia> search(String nombre) {
        try {
            return em.createQuery("select a from Provincia a where UPPER(a.nombre) LIKE CONCAT('%',UPPER(:nombre),'%')",
            Provincia.class).setParameter("nombre", nombre).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}