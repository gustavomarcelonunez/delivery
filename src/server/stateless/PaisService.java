package stateless;

import java.util.Collection;

import model.Pais;

public interface PaisService {

    public Collection<Pais> findAll();

    public Pais find(int id);

    public Pais findByNombre(String nombre);

    public Pais create(Pais pais);

    public Pais update(Pais pais);

    public Pais remove(int id);

    public Collection<Pais> search(String nombre);

    public ResultsPage<Pais> findByPage(Integer page, Integer cantPerPage);

}