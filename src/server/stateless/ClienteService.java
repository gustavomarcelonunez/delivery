package stateless;

import java.util.Collection;

import model.ArticulosPedido;
import model.Cliente;

public interface ClienteService {
    
    public Collection<Cliente> findAllClientes();

    public Cliente find(int id);

    public Cliente findByCuit(String cuit);

    public Cliente create(Cliente cliente);

    public Cliente update(Cliente cliente);

    public Cliente remove(int id);

    public Collection<Cliente> search(String razonSocial);

    public ResultsPage<Cliente> findByPage(Integer page, Integer cantPerPage);

	public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cantPerPage, Cliente cliente);
}
