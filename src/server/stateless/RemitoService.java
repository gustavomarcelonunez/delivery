package stateless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import model.ArticulosPedido;
import model.Remito;

public interface RemitoService {
    public ArrayList<Remito> generarRemitos(Date fechaArmado);

    public Collection<Remito> findAllRemitos();

    public Remito find(int id);

    public Remito updateEstado(Remito remito);

    public Remito create(Remito remito);

    public ResultsPage<Remito> findByPage(Integer page, Integer cantPerPage);

    public ResultsPage<ArticulosPedido> findAllArticulosPedido(Integer page, Integer cantPerPage, Remito remito);
}
