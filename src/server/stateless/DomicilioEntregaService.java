package stateless;

import model.DomicilioEntrega;
import java.util.Collection;

public interface DomicilioEntregaService {
    public DomicilioEntrega find(int id);

    public Collection<DomicilioEntrega> findAllDomiciliosEntrega();

    public DomicilioEntrega update(DomicilioEntrega domicilioEntrega);

    public DomicilioEntrega delete(int id);

    public Collection<DomicilioEntrega> search(String name);

    public ResultsPage<DomicilioEntrega> findByPage(Integer page, Integer cantPerPage);

    public DomicilioEntrega create(DomicilioEntrega domicilioEntrega);


}