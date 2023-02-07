package stateless;

import java.util.Collection;
import model.Articulo;

public interface ArticuloService {
  public Collection<Articulo> findAll();

  public Articulo find(int id);

  public Articulo findByCode(String codigo);

  public Articulo update(Articulo articulo);

  public Articulo updateStock(Articulo articulo, float newStock);

  public Articulo remove(int id);

  public Articulo create(Articulo articulo);

  public Collection<Articulo> search(String name);

  public ResultsPage<Articulo> findByPage(Integer page, Integer cantPerPage);
}
