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

import model.Localidad;
import stateless.LocalidadService;
import stateless.ResultsPage;

@Path("/localidades")
public class LocalidadRestServlet {

    @EJB
    private LocalidadService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public LocalidadRestServlet() {
        mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Localidad> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron las localidades", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") int id) {
        // Se modifica este método para que utilice el servicio
        Localidad localidad = service.find(id);

        if (localidad == null) {
            return ResponseMessage.message(505, "No existe localidad para el id indicado: " + id);
        }

        String data;
        try {
            data = mapper.writeValueAsString(localidad);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        return ResponseMessage.message(200, "Artículo recuperado con éxito", data);
        // return ResponseMessage.message(200, "Artículos recuperados con éxito", data);
    }

    @GET
    @Path("/search/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("search") String search) {

        // Se modifica este método para que utilice el servicio
        Collection<Localidad> localidades = service.search(search);

        if (localidades == null || localidades.isEmpty()) {
            return ResponseMessage.message(505, "No existe localidad para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(localidades);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron las localidades buscadas", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {
        Localidad localidad;
        String data;

        try {
            localidad = mapper.readValue(json, Localidad.class);

            localidad = service.update(localidad);

            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data = mapper.writeValueAsString(localidad);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "Localidad actualizada correctamente", data);
    }

    @DELETE
    @Path("/{id}")
    public String remove(@PathParam("id") int id) {
        Localidad localidad = service.remove(id);

        if (localidad == null)
            return ResponseMessage.message(503, "No se pudo eliminar la localidad " + id);

        return ResponseMessage.message(200, "Localidad eliminado exitosamente");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String json) {
        Localidad localidad;
        String data;

        try {
            localidad = mapper.readValue(json, Localidad.class);

            localidad = service.create(localidad);

            data = mapper.writeValueAsString(localidad);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó la localidad exitosamente", data);
    }

}