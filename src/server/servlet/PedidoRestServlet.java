package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
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
import model.Pedido;
import stateless.PedidoService;
import stateless.ResultsPage;

@Path("/pedidos")
public class PedidoRestServlet {

    @EJB
    private PedidoService service;
    private SimpleDateFormat df;
    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public PedidoRestServlet() {
        mapper = new ObjectMapper();

        // Le provee el formateador de fechas.
        df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant) {
        // Se modifica este método para que utilice el servicio
        ResultsPage<Pedido> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;
        Collection<Pedido> results = resultsPage.getResults().stream().map(ap -> {
            ap.getArticulosPedido().stream().map(app -> {
                app.setPedido(null);
                return app;
            }).collect(Collectors.toList());
            return ap;
        }).collect(Collectors.toList());

        resultsPage.setResults(results);

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los articulos", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") int id) {
        // Se modifica este método para que utilice el servicio
        Pedido pedido = service.find(id);

        if (pedido == null) {
            return ResponseMessage.message(505, "No existe pedido para el código indicado: " + id);
        }
        String data;

        Collection<ArticulosPedido> art = pedido.getArticulosPedido();

        if (art != null) {

            art.stream().map(ap -> {
                ap.setPedido(null);
                return ap;
            }).collect(Collectors.toList());

            try {
                data = mapper.writeValueAsString(pedido);
            } catch (JsonProcessingException e) {
                return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
            }

            data += ",\"total\": " + pedido.total();
            return ResponseMessage.message(200, "Pedido recuperado con éxito", data);
            // return ResponseMessage.message(200, "Artículos recuperados con éxito", data);
        } else
            return ResponseMessage.message(500, "No se pudieron recuperar los artículos pedidos");

    }

    // POST desarrollado para el test
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String sendPedido(String json) {

        Pedido pedido;
        String data;
        try {
            pedido = mapper.readValue(json, Pedido.class);
        } catch (IOException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida");
        }

        pedido = service.create(pedido);

        if (pedido != null) {

            data = "{\"pedido\":" + pedido.getId() + ",\"fecha\":\"" + df.format(pedido.getDate().getTime())
                    + "\",\"cliente\":" + pedido.getCliente().getCuit() + ", \"domicilio\": "
                    + pedido.getDomicilioEntrega().getId() + ",\"total\": " + pedido.total() + " }";

            return ResponseMessage.message(200, "Se cargó el pedido exitosamente", data);

        } else {

            return ResponseMessage.message(400, "El cliente NO existe.");
        }
    }

    // POST y PUT desarrollados para el front, lo que cambia es el path y el
    // resultado
    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPedido(String json) {
        Pedido pedido;
        String data;

        try {
            pedido = mapper.readValue(json, Pedido.class);

            pedido = service.create(pedido);

            Collection<ArticulosPedido> art = pedido.getArticulosPedido();

            if (art != null) {

                art.stream().map(ap -> {
                    ap.setPedido(null);
                    return ap;
                }).collect(Collectors.toList());
            }
            data = mapper.writeValueAsString(pedido);

        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el pedido exitosamenteeeee", data);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePedido(String json) {
        Pedido pedido;
        String data;

        try {
            pedido = mapper.readValue(json, Pedido.class);

            pedido = service.update(pedido);

            data = mapper.writeValueAsString(pedido);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se cargó el artículo exitosamente", data);
    }

    // @DELETE
    // @Path("/{id}")
    // public String remove(@PathParam("id") int id) {
    //     Pedido pedido = service.remove(id);

    //     if (pedido == null)
    //         return ResponseMessage.message(503, "No se pudo eliminar el pedido " + id);

    //     return ResponseMessage.message(200, "Se eliminó correctamente el pedido");
    // }

}