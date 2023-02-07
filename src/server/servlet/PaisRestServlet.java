package servlet;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Pais;
import stateless.PaisService;
import stateless.ResultsPage;

@Path("/paises")
public class PaisRestServlet {

    @EJB
    private PaisService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public PaisRestServlet() {
        mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Pais> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los países", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") int id) {
        // Se modifica este método para que utilice el servicio
        Pais pais = service.find(id);

        if (pais == null) {
            return ResponseMessage.message(505, "No existe país para el id indicado: " + id);
        }

        String data;
        try {
            data = mapper.writeValueAsString(pais);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        return ResponseMessage.message(200, "País recuperado con éxito", data);
        // return ResponseMessage.message(200, "Artículos recuperados con éxito", data);
    }

    @GET
    @Path("/search/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("search") String search) {

        // Se modifica este método para que utilice el servicio
        Collection<Pais> paises = service.search(search);

        if (paises == null || paises.isEmpty()) {
            return ResponseMessage.message(505, "No existe país para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(paises);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los paises buscados", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {
        Pais pais;
        String data;

        try {
            pais = mapper.readValue(json, Pais.class);

            pais = service.update(pais);

            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data = mapper.writeValueAsString(pais);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "País actualizada correctamente", data);
    }

    @DELETE
    @Path("/{id}")
    public String remove(@PathParam("id") int id) {
        Pais pais = service.remove(id);

        if (pais == null)
            return ResponseMessage.message(503, "No se pudo eliminar el país " + id);

        return ResponseMessage.message(200, "País eliminado exitosamente");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String json) {
        Pais pais;
        String data;

        try {
            pais = mapper.readValue(json, Pais.class);

            pais = service.create(pais);

            data = mapper.writeValueAsString(pais);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el país exitosamente", data);
    }

}