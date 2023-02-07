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
import model.Cliente;
import model.Factura;

@Stateless
public class FacturaServiceBean implements FacturaService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    // Inyecta el stateless de ArticulosPedido
    @EJB
    private ArticulosPedidoService articulosPedidoService;

    // Inyecta el stateless de Cliente
    @EJB
    private ClienteService clienteService;

    // Inyecta el stateless de Remito
    @EJB
    private RemitoService remitoService;

    @Override
    public Collection<Factura> findAllFacturas() {
        return em.createQuery("select f from Factura f", Factura.class).getResultList();
    }

    @Override
    public Factura find(int id) {
        return getEntityManager().find(Factura.class, id);
    }

    @Override
    public Factura create(Factura factura) {
        if (factura != null) {
            em.persist(factura);
            em.flush();
        }
        return factura;
    }

    @Override
    public ResultsPage<Factura> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Factura.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Factura.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Factura> resultPage = new ResultsPage<Factura>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cantPerPage, Factura factura) {
        /*
         * En este método, a diferencia de findByPage (línea 58) no se limita el número
         * máximo de resultados recuperados como se hace en la línea 63
         * "query.setMaxResults(cantPerPage);" Desconozco como hacer esto con una
         * Collection.
         */

        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSameFactura(factura.getId());
        Integer count = articulosPedido.size();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        ResultsPage<ArticulosPedido> resultPage = new ResultsPage<ArticulosPedido>(page, count,
                page > 1 ? page - 1 : page, page > lastPage ? page + 1 : lastPage, lastPage, articulosPedido);

        return resultPage;
    }

    @Override
    public ArrayList<Factura> generarFacturas(Date fechaFactura) {
        ArrayList<Factura> facturas = new ArrayList<Factura>();

        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSinFacturar();

        Cliente cliente = null;
        Factura factura = null;
        int contador = 0;
        for (ArticulosPedido art : articulosPedido) {
            // si cambio el cliente, crear una factura nueva

            if (!art.getPedido().getCliente().equals(cliente)) {
                if (factura != null && contador > 0) {
                    this.create(factura);
                    facturas.add(factura);
                }

                cliente = clienteService.find(art.getPedido().getCliente().getId());

                contador = 0;
                factura = new Factura();
                factura.setFechaEmision(fechaFactura);
                factura.setCliente(cliente);
            }

            art.setFactura(factura);
            articulosPedidoService.update(art);
            contador++;
        }
        if (factura != null && contador > 0) {
            this.create(factura);
            facturas.add(factura);
        }
        return facturas;
    }

    @Override
    public double calcularTotal(int id) {

        double total = 0;
        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSameFactura(id);
        for (ArticulosPedido art : articulosPedido) {
            total += (art.getPrecioUnitario() * art.getCantidad());

        }
        total = Math.round(total * 100d) / 100d;
        return total;
    }

    public Factura updateFechaPago(Factura factura, Date cal) {
        Factura fact = factura;
        fact.setFechaPago(cal);
        em.merge(factura);
        return fact;
    }
}
