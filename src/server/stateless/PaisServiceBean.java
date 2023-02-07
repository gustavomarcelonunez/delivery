package stateless;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Pais;

@Stateless
public class PaisServiceBean implements PaisService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Pais find(int id) {
        return getEntityManager().find(Pais.class, id);
    }

    @Override
    public Collection<Pais> findAll() {
        return em.createQuery("select l from Pais l", Pais.class).getResultList();
    }

    @Override
    public ResultsPage<Pais> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Pais.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Pais.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Pais> resultPage = new ResultsPage<Pais>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public Pais findByNombre(String nombre) {
        try {
            return em.createQuery("select l from Pais l where l.nombre = :nombre", Pais.class)
                    .setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Pais update(Pais pais) {
        em.merge(pais);
        return pais;
    }

    @Override
    public Pais remove(int id) {
        Pais pais = em.find(Pais.class, id);
        if (pais != null)
            em.remove(pais);

        return pais;
    }

    public Pais create(Pais pais) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        em.persist(pais); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return pais;
    }

    @Override
    public Collection<Pais> search(String nombre) {
        try {
            return em.createQuery("select a from Pais a where UPPER(a.nombre) LIKE CONCAT('%',UPPER(:nombre),'%')",
                    Pais.class).setParameter("nombre", nombre).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}