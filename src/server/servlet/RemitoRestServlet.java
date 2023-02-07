package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import model.Remito;
import stateless.RemitoService;
import stateless.ResultsPage;

@Path("/remitos")
public class RemitoRestServlet {
    @EJB
    private RemitoService service;

    private SimpleDateFormat df;
    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public RemitoRestServlet() {
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
        ResultsPage<Remito> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron los remitos", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findRemito(@PathParam("id") int id) throws IOException {
        Remito remito = service.find(id);
        String data;
        try {
            data = mapper.writeValueAsString(remito);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        if (remito == null)
            return ResponseMessage.message(501, "No se encontró el remito " + remito);

        return ResponseMessage.message(200, "Remito recuperado con éxito", data);
    }

    @GET
    @Path("/{id}/articulospedido")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllArticulosPedido(@DefaultValue("1") @QueryParam("page") Integer page,
    @DefaultValue("10") @QueryParam("cant") Integer cant, @PathParam("id") int id) throws IOException {

        Remito remito = service.find(id);

        String data;
        if (remito != null) {

            ResultsPage<ArticulosPedido> art = service.findAllArticulosPedido(page, cant, remito);

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
                return "{\"StatusCode\":501,\"StatusText\":\"El Remito NO contiene artículos pedido.\"}";

            }

        } else
            return "{\"StatusCode\":500,\"StatusText\": \"El Remito NO existe.\"}";
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEstado(String json) {
        Remito remito;
        String data;
        try {
            remito = mapper.readValue(json, Remito.class);
            remito = service.find(remito.getId());
            remito = service.updateEstado(remito);
            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data = mapper.writeValueAsString(remito);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "Remito entregado correctamente", data);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String generarRemitos(String fechaArmado) throws IOException {
        Date cal;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal = sdf.parse(mapper.readValue(fechaArmado, String.class));
        } catch (Exception e) {
            return ResponseMessage.message(501, "No se pudo dar formato a la fecha", e.getMessage());
        }

        ArrayList<Remito> remitos = service.generarRemitos(cal);

        if (!remitos.isEmpty()) {

            String x = "{\"cantidad\": " + remitos.size() + "}";

            return ResponseMessage.message(200, "Remitos generados exitosamente", x);
        } else {
            return ResponseMessage.message(400, "No se generó ningún remito.");
        }
    }
}
