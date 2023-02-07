package stateless;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ValidationException;

import model.Articulo;
import model.ArticulosPedido;
import model.Cliente;
import model.DomicilioEntrega;
import model.Pedido;

@Stateless
public class PedidoServiceBean implements PedidoService {
    @PersistenceContext(unitName = "delivery")
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    // Inyecta el stateless de Articulo
    @EJB
    private ArticuloService articuloService;

    // Inyecta el stateless de Cliente
    @EJB
    private ClienteService clienteService;

    // Inyecta el stateless de DomicilioEntrega
    @EJB
    private DomicilioEntregaService domicilioEntregaService;

    // Inyecta el stateless de ArticulosPedido
    @EJB
    private ArticulosPedidoService articulosPedidoService;

    // Inyecta el stateless de Localidad
    @EJB
    private LocalidadService localidadService;

    public Pedido create(Pedido pedido) {
        if (pedido == null) {
            throw new ValidationException("Pedido inexistente");
        }
        Cliente cliente = clienteService.findByCuit(pedido.getCliente().getCuit());
        // Aqui verifica si el cliente no existe
        if (cliente == null) {
            // Se utilizará esta validación cuando no se use mas el test
            // throw new ValidationException("Cliente inexistente");
            return null;
        }
        pedido.setCliente(cliente);

        DomicilioEntrega domicilioEntrega = domicilioEntregaService.find(pedido.getDomicilioEntrega().getId());
        if (domicilioEntrega == null) {
            throw new ValidationException("Domicilio Entrega inexistente");
        }
        pedido.setDomicilioEntrega(domicilioEntrega);

        for (ArticulosPedido ap : pedido.getArticulosPedido()) {
            ap.setPedido(pedido);

            Articulo articulo = articuloService.findByCode(ap.getArticulo().getCodigo());

            if (articulo == null) {
                throw new ValidationException("Artículo inexistente");
            }
            ap.setArticulo(articulo);
            ap.setPrecioUnitario(articulo.getPrecio());
            ap.setPrecio(ap.getPrecioUnitario() * ap.getCantidad());
        }
        // Aquí debe PERSISTIR en pedido en la base de datos
        em.persist(pedido); // <-- Aquí es donde le decimos a JPA que persista el dato.
        return pedido;
    }

    public Pedido find(int id) {
        return getEntityManager().find(Pedido.class, id);
    }

    public Collection<Pedido> findAll() {
        return em.createQuery("select e from Pedido e", Pedido.class).getResultList();
    }

    // Aun no se utiliza
    public Pedido update(Pedido pedido) {
        em.merge(pedido);
        return pedido;
    }

    @Override
    public ResultsPage<Pedido> findByPage(Integer page, Integer cantPerPage) {
        // Cuento el total de resultados
        Query countQuery = em.createQuery("SELECT COUNT(p.id) FROM " + Pedido.class.getSimpleName() + " p");
        // Pagino
        Query query = em.createQuery("FROM " + Pedido.class.getSimpleName() + " e");
        query.setMaxResults(cantPerPage);
        query.setFirstResult((page - 1) * cantPerPage);
        Integer count = ((Long) countQuery.getSingleResult()).intValue();
        Integer lastPage = (int) Math.ceil((double) count / (double) cantPerPage);

        // Armo respuesta
        ResultsPage<Pedido> resultPage = new ResultsPage<Pedido>(page, count, page > 1 ? page - 1 : page,
                page > lastPage ? page + 1 : lastPage, lastPage, query.getResultList());
        return resultPage;
    }

    // @Override
    // public Pedido remove(int id) {
    //     Pedido pedido = em.find(Pedido.class, id);
    //     for (ArticulosPedido ap : pedido.getArticulosPedido()) {
    //         articulosPedidoService.remove(ap.getId());
    //     }
    //     if (pedido != null)
    //         em.remove(pedido);

    //     return pedido;
    // }

    @Override
    public Collection<Pedido> search(String cliente) {
        try {
            return em.createQuery(
                    "select p from Pedido p where UPPER(p.cliente.razonSocial) LIKE CONCAT('%',UPPER(:cliente),'%')",
                    Pedido.class).setParameter("cliente", cliente).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
