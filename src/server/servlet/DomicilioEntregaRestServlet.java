package servlet;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.DomicilioEntrega;
import stateless.DomicilioEntregaService;
import stateless.ResultsPage;

@Path("/domicilios")
public class DomicilioEntregaRestServlet {

    @EJB
    private DomicilioEntregaService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public DomicilioEntregaRestServlet() {
        mapper = new ObjectMapper();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllDomiciliosEntrega(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<DomicilioEntrega> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;
        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        return ResponseMessage.message(200, "DomiciliosEntrega recuperados con éxito", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") int id) throws IOException {
        DomicilioEntrega domicilioEntrega = service.find(id);
        String data;
        try {
            data = mapper.writeValueAsString(domicilioEntrega);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        if (domicilioEntrega == null)
            return ResponseMessage.message(501, "No se encontró el domicilio " + id);

        return ResponseMessage.message(200, "Domicilio recuperado con éxito", data);
    }

    @GET
    @Path("/search/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("search") String search) {

        // Se modifica este método para que utilice el servicio
        Collection<DomicilioEntrega> domicilios = service.search(search);

        if (domicilios == null || domicilios.isEmpty()) {
            return ResponseMessage.message(505, "No existe domicilios para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(domicilios);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los domicilios buscadas", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {

        DomicilioEntrega domicilioEntrega;
        String data;

        try {
            domicilioEntrega = mapper.readValue(json, DomicilioEntrega.class);

            domicilioEntrega = service.update(domicilioEntrega);

            data = mapper.writeValueAsString(domicilioEntrega);

        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el domicilio exitosamente", data);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") int id) {
        DomicilioEntrega domicilioEntrega = service.delete(id);

        if (domicilioEntrega == null)
            return ResponseMessage.message(503, "No se pudo eliminar el domicilio " + id);

        return ResponseMessage.message(200, "DomicilioEntrega eliminado exitosamente", "{}");
    }
}
