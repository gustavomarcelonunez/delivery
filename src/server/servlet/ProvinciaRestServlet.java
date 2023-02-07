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

import model.Provincia;
import stateless.ProvinciaService;
import stateless.ResultsPage;

@Path("/provincias")
public class ProvinciaRestServlet {

    @EJB
    private ProvinciaService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public ProvinciaRestServlet() {
        mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Provincia> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron las provincias", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") int id) {
        // Se modifica este método para que utilice el servicio
        Provincia provincia = service.find(id);

        if (provincia == null) {
            return ResponseMessage.message(505, "No existe provincia para el id indicado: " + id);
        }

        String data;
        try {
            data = mapper.writeValueAsString(provincia);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        return ResponseMessage.message(200, "Provincia recuperado con éxito", data);
        // return ResponseMessage.message(200, "Artículos recuperados con éxito", data);
    }

    @GET
    @Path("/search/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("search") String search) {

        // Se modifica este método para que utilice el servicio
        Collection<Provincia> provincias = service.search(search);

        if (provincias == null || provincias.isEmpty()) {
            return ResponseMessage.message(505, "No existe provincia para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(provincias);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron las provincias buscadas", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {
        Provincia provincia;
        String data;

        try {
            provincia = mapper.readValue(json, Provincia.class);

            provincia = service.update(provincia);

            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data = mapper.writeValueAsString(provincia);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "Provincia actualizada correctamente", data);
    }

    @DELETE
    @Path("/{id}")
    public String remove(@PathParam("id") int id) {
        Provincia provincia = service.remove(id);

        if (provincia == null)
            return ResponseMessage.message(503, "No se pudo eliminar la provincia " + id);

        return ResponseMessage.message(200, "Provincia eliminado exitosamente");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String json) {
        Provincia provincia;
        String data;

        try {
            provincia = mapper.readValue(json, Provincia.class);

            provincia = service.create(provincia);

            data = mapper.writeValueAsString(provincia);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó la provincia exitosamente", data);
    }

}