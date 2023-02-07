package stateless;

import java.util.Collection;
import model.Pedido;

public interface PedidoService {

    public Collection<Pedido> findAll();

    public Pedido find(int id);

    public Pedido create(Pedido pedido);

    public Pedido update(Pedido pedido);

    // public Pedido remove(int id);

    public Collection<Pedido> search(String name);

    public ResultsPage<Pedido> findByPage(Integer page, Integer cantPerPage);
}
