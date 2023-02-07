package stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.ArticulosPedido;
import model.DomicilioEntrega;
import model.Remito;

@Stateless
public class RemitoServiceBean implements RemitoService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    // Inyecta el stateless de Cliente
    @EJB
    private ClienteService clienteService;

    // Inyecta el stateless de Pedido
    @EJB
    private PedidoService pedidoService;

    // Inyecta el stateless de ArticulosPedido
    @EJB
    private ArticulosPedidoService articulosPedidoService;

    // Inyecta el statless de DomicilioEntrega
    @EJB
    private DomicilioEntregaService domicilioEntregaService;

    // Inyecta el statless de Articulo
    @EJB
    private ArticuloService articuloService;

    @Override
    public Collection<Remito> findAllRemitos() {
        return em.createQuery("select r from Remito r", Remito.class).getResultList();
    }

    @Override
    public Remito find(int id) {
        return getEntityManager().find(Remito.class, id);
    }

    @Override
    public Remito updateEstado(Remito remito) {
        if (remito != null) {
            if (remito.getEntregado() == false) {
                remito.setEntregado(true);
            } else {
                remito.setEntregado(false);
            }
            em.merge(remito);
        }
        return remito;
    }

    @Override
    public Remito create(Remito remito) {
        if (remito != null) {
            em.persist(remito);
            em.flush();
        }
        return remito;
    }

    @Override
    public ResultsPage<Remito> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Remito.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Remito.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Remito> resultPage = new ResultsPage<Remito>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cantPerPage, Remito remito) {
        /*
         * En este método, a diferencia de findByPage (línea 79) no se limita el número
         * máximo de resultados recuperados como se hace en la línea 84
         * "query.setMaxResults(cantPerPage);" Desconozco como hacer esto con una
         * Collection.
         */

        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSameRemito(remito.getId());
        Integer count = articulosPedido.size();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        ResultsPage<ArticulosPedido> resultPage = new ResultsPage<ArticulosPedido>(page, count,
                page > 1 ? page - 1 : page, page > lastPage ? page + 1 : lastPage, lastPage, articulosPedido);

        return resultPage;
    }

    @Override
    public ArrayList<Remito> generarRemitos(Date fechaArmado) {
        ArrayList<Remito> remitos = new ArrayList<Remito>();

        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSinRemitoHasta(fechaArmado);

        DomicilioEntrega dom = null;
        Remito remito = null;
        int contador = 0;
        for (ArticulosPedido art : articulosPedido) {
            // si cambio el domicilio, crear uno nuevo

            if (!art.getPedido().getDomicilioEntrega().equals(dom)) {
                if (remito != null && contador > 0) {
                    this.create(remito);
                    remito.setEntregado(false);
                    remitos.add(remito);
                }

                dom = domicilioEntregaService.find(art.getPedido().getDomicilioEntrega().getId());

                contador = 0;
                remito = new Remito();
                remito.setFechaArmado(fechaArmado);
                remito.setDomicilioEntrega(domicilioEntregaService.find(dom.getId()));
            }

            if (art.getArticulo().getStock() >= art.getCantidad()) {
                art.setRemito(remito);
                articulosPedidoService.update(art);
                articuloService.updateStock(art.getArticulo(), art.getArticulo().getStock() - art.getCantidad());
                ++contador;
            }
        }

        if (remito != null && contador > 0) {
            this.create(remito);
            remito.setEntregado(false);
            remitos.add(remito);
        }

        return remitos;
    }

}
