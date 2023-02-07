package stateless;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Articulo;

@Stateless
public class ArticuloServiceBean implements ArticuloService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Articulo find(int id) {
        return getEntityManager().find(Articulo.class, id);
    }

    @Override
    public ResultsPage<Articulo> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Articulo.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Articulo.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Articulo> resultPage = new ResultsPage<Articulo>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public Articulo findByCode(String codigo) {
        try {
            return em.createQuery("select a from Articulo a where a.codigo = :codigo", Articulo.class)
                    .setParameter("codigo", codigo).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<Articulo> findAll() {
        return em.createQuery("select a from Articulo a", Articulo.class).getResultList();
    }

    @Override
    public Articulo update(Articulo articulo) {
        em.merge(articulo);
        return articulo;
    }

    @Override
    public Articulo remove(int id) {
        Articulo articulo = em.find(Articulo.class, id);
        if (articulo != null)
            em.remove(articulo);

        return articulo;
    }

    @Override
    public Collection<Articulo> search(String nombre) {
        try {
            return em.createQuery("select a from Articulo a where UPPER(a.nombre) LIKE CONCAT('%',UPPER(:nombre),'%')",
                    Articulo.class).setParameter("nombre", nombre).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Articulo create(Articulo articulo) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        em.persist(articulo); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return articulo;
    }

    @Override
    public Articulo updateStock(Articulo articulo, float newStock) {
        articulo.setStock(newStock);
        this.update(articulo);
        return articulo;
    }
}
