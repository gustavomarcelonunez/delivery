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
import model.Factura;
import stateless.FacturaService;
import stateless.ResultsPage;

@Path("/facturas")
public class FacturaRestServlet {
    @EJB
    private FacturaService service;

    private SimpleDateFormat df;
    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public FacturaRestServlet() {
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
        ResultsPage<Factura> resultsPage = service.findByPage(page, cant);

        // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
        String data;

        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Se recuperaron las facturas", data);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findFactura(@PathParam("id") int id) throws IOException {
        Factura factura = service.find(id);
        String data;
        try {
            data = mapper.writeValueAsString(factura);

        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        }

        if (factura == null) {
            data = "{}";
            return ResponseMessage.message(501, "La factura " + id + " no existe", data);
        }

        data += ",\"total\": " + service.calcularTotal(id);
        return ResponseMessage.message(200, "Factura recuperado con éxito", data);
    }

    @GET
    @Path("/{id}/articulospedido")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllArticulosPedido(@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("10") @QueryParam("cant") Integer cant, @PathParam("id") int id) throws IOException {

        Factura factura = service.find(id);

        String data;
        if (factura != null) {

            ResultsPage<ArticulosPedido> art = service.findAllArticulosPedido(page, cant, factura);

            if (art != null) {

                Collection<ArticulosPedido> results = art.getResults().stream().map(ap -> {
                    ap.getPedido().setArticulosPedido(null);

                    return ap;
                }).collect(Collectors.toList());

                art.setResults(results);

                try {
                    data = mapper.writeValueAsString(art);
                    data += ",\"total\": " + service.calcularTotal(id);
                } catch (JsonProcessingException e) {
                    return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
                }
                
                return ResponseMessage.message(200, "Artículos pedidos recuperado con éxito", data);

            } else {
                return "{\"StatusCode\":501,\"StatusText\":\"La factura NO contiene artículos pedido.\"}";

            }

        } else
            return "{\"StatusCode\":500,\"StatusText\": \"La factura NO existe.\"}";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String generarFacturas(String fechaFactura) throws IOException {
        Date cal;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal = sdf.parse(mapper.readValue(fechaFactura, String.class));
        } catch (Exception e) {
            return ResponseMessage.message(501, "No se pudo dar formato a la fecha", e.getMessage());
        }

        ArrayList<Factura> facturas = service.generarFacturas(cal);

        if (!facturas.isEmpty()) {

            String x = "{\"cantidad\": " + facturas.size() + "}";

            return ResponseMessage.message(200, "Facturas generadas exitosamente", x);
        } else {
            return ResponseMessage.message(400, "No se generó ninguna factura.");
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFechaPago(String json) {
        Factura factura;
        String data = "{\"factura\": ";
        try {
            factura = mapper.readValue(json, Factura.class);
            Date cal = factura.getFechaPago();
            factura = service.find(factura.getId());
            factura = service.updateFechaPago(factura, cal);
            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            data += +factura.getId() + "}";
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }
        return ResponseMessage.message(200, "Factura abonada correctamente", data);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String fechaPago, @PathParam("id") int id) {
        Factura factura = service.find(id);
        String data;
        Date cal;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal = sdf.parse(mapper.readValue(fechaPago, String.class));
        } catch (Exception e) {
            return ResponseMessage.message(501, "No se pudo dar formato a la fecha", e.getMessage());
        }
        try {
            factura = service.updateFechaPago(factura, cal);
            // Se contruye el resultado en base a lo recuperado desde la capa de negocio.
            
            data = mapper.writeValueAsString(factura);
        } catch (JsonProcessingException e) {
            return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
        } 
        return ResponseMessage.message(200, "Factura abonada correctamente", data);
    }
}