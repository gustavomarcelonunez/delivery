package stateless;

import java.util.Collection;

import model.Provincia;

public interface ProvinciaService {

    public Collection<Provincia> findAll();

    public Provincia find(int id);

    public Provincia findByNombre(String nombre);

    public Provincia create(Provincia provincia);

    public Provincia update(Provincia provincia);

    public Provincia remove(int id);

    public Collection<Provincia> search(String nombre);

    public ResultsPage<Provincia> findByPage(Integer page, Integer cantPerPage);

}