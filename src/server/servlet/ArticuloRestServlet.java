package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import model.Articulo;
import stateless.ArticuloService;
import stateless.ResultsPage;

@Path("/articulos")
public class ArticuloRestServlet {
    @EJB
    private ArticuloService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public ArticuloRestServlet() {
        mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        // Le provee el formateador de fechas.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Articulo> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los articulos", data);
    }

    @GET
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findByCode(@PathParam("codigo") String codigo) {
        // Se modifica este método para que utilice el servicio
        Articulo articulo = service.findByCode(codigo);

        if (articulo == null) {
            return ResponseMessage.message(505, "No existe artículo para el código indicado: " + codigo);
        }

        String data;
        try {
            data = mapper.writeValueAsString(articulo);
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
        Collection<Articulo> articulos = service.search(search);

        if (articulos == null || articulos.isEmpty()) {
            return ResponseMessage.message(505, "No existe artículo para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(articulos);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los artículos buscados", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateStock(String json) {
        Articulo articulo;
        String data;
        float nuevoStock;

        try {
            articulo = mapper.readValue(json, Articulo.class);
            nuevoStock = articulo.getStock();
            articulo = service.findByCode(articulo.getCodigo());
            articulo = service.updateStock(articulo, articulo.getStock() + nuevoStock);
            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data = mapper.writeValueAsString(articulo);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "Stock actualizado correctamente", data);
    }

    @DELETE
    @Path("/{id}")
    public String remove(@PathParam("id") int id) {
        Articulo articulo = service.remove(id);

        if (articulo == null)
            return ResponseMessage.message(503, "No se pudo eliminar el artículo " + id);

        return ResponseMessage.message(200, "Se eliminó correctamente el artículo");
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String json) {
        Articulo articulo;
        String data;

        try {
            articulo = mapper.readValue(json, Articulo.class);

            articulo = service.create(articulo);

            data = mapper.writeValueAsString(articulo);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el artículo exitosamente", data);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {
        Articulo articulo;
        String data;

        try {
            articulo = mapper.readValue(json, Articulo.class);

            articulo = service.update(articulo);

            data = mapper.writeValueAsString(articulo);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el artículo exitosamente", data);
    }

}
