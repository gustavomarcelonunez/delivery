package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import model.ArticulosPedido;
import model.Cliente;
import stateless.ClienteService;
import stateless.ResultsPage;

@Path("/clientes")
public class ClienteRestServlet {
    @EJB
    private ClienteService service;

    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public ClienteRestServlet() {
        mapper = new ObjectMapper();

        // Le provee el formateador de fechas.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllClientes(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Cliente> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;
        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        return ResponseMessage.message(200, "Clientes recuperados con éxito", data);
    }

    @GET
    @Path("/{cuit}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findByCuit(@PathParam("cuit") String cuit) throws IOException {
        Cliente cliente = service.findByCuit(cuit);
        String data;
        try {
            data = mapper.writeValueAsString(cliente);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        if (cliente == null)
            return ResponseMessage.message(501, "No se encontró el cliente " + cuit);

        return ResponseMessage.message(200, "Cliente recuperado con éxito", data);
    }

    @GET
    @Path("/{cuit}/domicilios")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllDomiciliosEntrega(@PathParam("cuit") String cuit) throws IOException {
        Cliente cliente = service.findByCuit(cuit);

        if (cliente != null) {
            String response = "{\"domicilios\": ";
            try {
                response += mapper.writeValueAsString(cliente.getDomiciliosEntrega());
            } catch (JsonProcessingException e) {
                return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
            }

            response += "}";

            return ("{\"StatusCode\":200,\"StatusText\":\"domicilios del cliente recuperados exitosamente\",\"data\":"
                    + response + "}");
        } else
            return "{\"StatusCode\":500,\"StatusText\": \"El Cliente NO existe.\"}";
    }

    @GET
    @Path("/{cuit}/articulospedido")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllArticulosPedido(@DefaultValue("1") @QueryParam("page") Integer page,
    @DefaultValue("10") @QueryParam("cant") Integer cant, @PathParam("cuit") String cuit) throws IOException {

        Cliente cliente = service.findByCuit(cuit);

        String data;
        if (cliente != null) {

            ResultsPage<ArticulosPedido> art = service.findAllArticulosPedido(page, cant, cliente);

            if (art != null) {

                Collection<ArticulosPedido> results = art.getResults().stream().map(ap -> {
                    ap.getPedido().setArticulosPedido(null);

                    return ap;
                }).collect(Collectors.toList());

                art.setResults(results);

                try {
                    data = mapper.writeValueAsString(art);
                } catch (JsonProcessingException e) {
                    return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
                }
                return ResponseMessage.message(200, "Artículos pedidos recuperado con éxito", data);

            } else {
                return "{\"StatusCode\":501,\"StatusText\":\"El Cliente NO ha realizado ningún pedido.\"}";

            }

        } else
            return "{\"StatusCode\":500,\"StatusText\": \"El Cliente NO existe.\"}";
    }

    @GET
    @Path("/search/{search}")
    @Produces(MediaType.APPLICATION_JSON)
    public String search(@PathParam("search") String search) {

        // Se modifica este método para que utilice el servicio
        Collection<Cliente> clientes = service.search(search);

        if (clientes == null || clientes.isEmpty()) {
            return ResponseMessage.message(505, "No existe cliente para la búsqueda indicada: " + search);
        }

        String data;
        try {

            data = mapper.writeValueAsString(clientes);

        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los clientes buscados", data);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String json) {
        Cliente cliente;
        String data;

        try {
            cliente = mapper.readValue(json, Cliente.class);

            cliente = service.create(cliente);

            data = mapper.writeValueAsString(cliente);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el cliente exitosamente", data);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String json) {
        Cliente cliente;
        String data;

        try {
            cliente = mapper.readValue(json, Cliente.class);

            cliente = service.update(cliente);

            data = mapper.writeValueAsString(cliente);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el cliente exitosamente", data);
    }

    @DELETE
    @Path("/{id}")
    public String remove(@PathParam("id") int id) {
        Cliente cliente = service.remove(id);

        if (cliente == null)
            return ResponseMessage.message(503, "No se pudo eliminar el cliente " + id);

        return ResponseMessage.message(200, "Cliente eliminado exitosamente");
    }
}
