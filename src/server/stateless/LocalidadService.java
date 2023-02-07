package stateless;

import java.util.Collection;

import model.Localidad;

public interface LocalidadService {

    public Collection<Localidad> findAll();

    public Localidad find(int id);

    public Localidad findByNombre(String nombre);

    public Localidad create(Localidad localidad);

    public Localidad update(Localidad localidad);

    public Localidad remove(int id);

    public Collection<Localidad> search(String nombre);

    public ResultsPage<Localidad> findByPage(Integer page, Integer cantPerPage);

}