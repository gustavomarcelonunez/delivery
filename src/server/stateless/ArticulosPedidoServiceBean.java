package stateless;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.ArticulosPedido;

@Stateless
public class ArticulosPedidoServiceBean implements ArticulosPedidoService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public Collection<ArticulosPedido> findAllArticulosPedido() {
        return em.createQuery("select ap from ArticulosPedido ap", ArticulosPedido.class).getResultList();
    }

    @Override
    public ArticulosPedido findArticulosPedido(int id) {
        return getEntityManager().find(ArticulosPedido.class, id);
    }

    @Override
    public Collection<ArticulosPedido> findSinRemitoHasta(Date date) {
        try {
            return getEntityManager().createNamedQuery("ArticulosPedido.findSinRemitoHasta", ArticulosPedido.class)
                    .setParameter("date", date).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<ArticulosPedido> findSinFacturar() {
        try {
            return getEntityManager().createNamedQuery("ArticulosPedido.findSinFacturar", ArticulosPedido.class)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<ArticulosPedido> findSameRemito(int id) {
        try {
            return getEntityManager().createNamedQuery("ArticulosPedido.findSameRemito", ArticulosPedido.class)
                    .setParameter("id", id).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<ArticulosPedido> findSameFactura(int id) {
        try {
            return getEntityManager().createNamedQuery("ArticulosPedido.findSameFactura", ArticulosPedido.class)
                    .setParameter("id", id).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<ArticulosPedido> findSameCliente(String cuit) {
        try {
            return getEntityManager().createNamedQuery("ArticulosPedido.findSameCliente", ArticulosPedido.class)
                    .setParameter("cuit", cuit).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ResultsPage<ArticulosPedido> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + ArticulosPedido.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + ArticulosPedido.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<ArticulosPedido> resultPage = new ResultsPage<ArticulosPedido>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public ArticulosPedido update(ArticulosPedido articulosPedido) {
        em.merge(articulosPedido);
        return articulosPedido;
    }

    @Override
    public ArticulosPedido remove(int id) {
        ArticulosPedido articulosPedido = em.find(ArticulosPedido.class, id);
        if (articulosPedido != null)
            em.remove(articulosPedido);

        return articulosPedido;
    }
}
