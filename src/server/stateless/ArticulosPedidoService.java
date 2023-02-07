package stateless;

import java.util.Date;
import java.util.Collection;
import model.ArticulosPedido;

public interface ArticulosPedidoService {
    
    public Collection<ArticulosPedido> findAllArticulosPedido();

    public ArticulosPedido findArticulosPedido(int id);

    public ArticulosPedido remove(int id);

    public ArticulosPedido update(ArticulosPedido articulosPedido);

    public Collection<ArticulosPedido> findSinRemitoHasta(Date date);

    public Collection<ArticulosPedido> findSinFacturar();

    public Collection<ArticulosPedido> findSameRemito(int id);

    public Collection<ArticulosPedido> findSameFactura(int id);

    public Collection<ArticulosPedido> findSameCliente(String cuit);

	public ResultsPage<ArticulosPedido> findByPage(Integer page, Integer cant);

}
