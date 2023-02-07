package stateless;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.DomicilioEntrega;

@Stateless
public class DomicilioEntregaServiceBean implements DomicilioEntregaService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Collection<DomicilioEntrega> findAllDomiciliosEntrega() {
        return em.createQuery("select de from DomicilioEntrega de", DomicilioEntrega.class).getResultList();
    }

    @Override
    public DomicilioEntrega find(int id) {
        return getEntityManager().find(DomicilioEntrega.class, id);
    }

    @Override
    public DomicilioEntrega update(DomicilioEntrega domicilioEntrega) {
        em.persist(domicilioEntrega);
        return domicilioEntrega;
    }

    @Override
    public DomicilioEntrega delete(int id) {

        DomicilioEntrega domicilioEntrega = em.find(DomicilioEntrega.class, id);

        if (domicilioEntrega != null)
            em.remove(domicilioEntrega);

        return domicilioEntrega;
    }

    @Override
    public ResultsPage<DomicilioEntrega> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + DomicilioEntrega.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + DomicilioEntrega.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<DomicilioEntrega> resultPage = new ResultsPage<DomicilioEntrega>(page, count,
                page > 1 ? page - 1 : page, page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public Collection<DomicilioEntrega> search(String calle) {
        try {
            return em.createQuery(
                    "select de from DomicilioEntrega de where UPPER(de.calle) LIKE CONCAT('%',UPPER(:calle),'%')",
                    DomicilioEntrega.class).setParameter("calle", calle).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public DomicilioEntrega create(DomicilioEntrega domicilioEntrega) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        em.persist(domicilioEntrega); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return domicilioEntrega;
    }

}