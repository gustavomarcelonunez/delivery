package stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import model.ArticulosPedido;
import model.Factura;

public interface FacturaService {

    public Collection<Factura> findAllFacturas();

    public Factura find(int id);

    public double calcularTotal(int id);

    public Factura create(Factura factura);

    public ArrayList<Factura> generarFacturas(Date fechaFactura);

    public Factura updateFechaPago(Factura factura, Date cal);

    public ResultsPage<Factura> findByPage(Integer page, Integer cantPerPage);

	public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cant, Factura factura);

}