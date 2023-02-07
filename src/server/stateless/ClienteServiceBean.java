package stateless;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.ArticulosPedido;
import model.Cliente;

@Stateless
public class ClienteServiceBean implements ClienteService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    // Inyecta el stateless de DomicilioEntrega
    @EJB
    private DomicilioEntregaService domicilioEntregaService;

    // Inyecta el stateless de Localidad
    @EJB
    private LocalidadService localidadService;

    // Inyecta el stateless de ArticulosPedido
    @EJB
    private ArticulosPedidoService articulosPedidoService;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public ResultsPage<Cliente> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(e.id) FROM " + Cliente.class.getSimpleName() + " e");
        // Pagino
        Query query = em.createQuery("FROM " + Cliente.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Cliente> resultPage = new ResultsPage<Cliente>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    @Override
    public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cantPerPage, Cliente cliente) {
        /* En este método, a diferencia de findByPage (línea 38) no se limita el número máximo de resultados recuperados
          como se hace en la línea 43  "query.setMaxResults(cantPerPage);" Desconozco como hacer esto con una Collection.
        */

        Collection<ArticulosPedido> articulosPedido = articulosPedidoService.findSameCliente(cliente.getCuit());
        Integer count = articulosPedido.size();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        ResultsPage<ArticulosPedido> resultPage = new ResultsPage<ArticulosPedido>(page, count,
                page > 1 ? page - 1 : page, page > lastPage ? page + 1 : lastPage, lastPage, articulosPedido);

        return resultPage;
    }

    @Override
    public Cliente find(int id) {
        return getEntityManager().find(Cliente.class, id);
    }

    @Override
    public Cliente findByCuit(String cuit) {
        try {
            return em.createQuery("select e from Cliente e where e.cuit = :cuit", Cliente.class)
                    .setParameter("cuit", cuit).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Cliente remove(int id) {
        Cliente cliente = em.find(Cliente.class, id);
        if (cliente != null)
            em.remove(cliente);

        return cliente;
    }

    @Override
    public Collection<Cliente> findAllClientes() {
        return em.createQuery("select e from Cliente e", Cliente.class).getResultList();
    }

    @Override
    public Cliente update(Cliente cliente) {
        em.merge(cliente);
        return cliente;
    }

    @Override
    public Collection<Cliente> search(String razonSocial) {
        try {
            return em.createQuery(
                    "select c from Cliente c where UPPER(c.razonSocial) LIKE CONCAT('%',UPPER(:razonSocial),'%')",
                    Cliente.class).setParameter("razonSocial", razonSocial).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Cliente create(Cliente cliente) {
        // Aquí debe PERSISTIR en borderó en la base de datos
        cliente.setDomiciliosEntrega(cliente.getDomiciliosEntrega().stream().map(dom -> {
            dom.setLocalidad(localidadService.findByNombre(dom.getLocalidad().getNombre()));
            return dom;
        }).collect(Collectors.toList()));

        em.persist(cliente); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return cliente;
    }

}
