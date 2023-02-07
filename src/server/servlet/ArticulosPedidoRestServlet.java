package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.ArticulosPedido;
import stateless.ArticulosPedidoService;
import stateless.ResultsPage;

@Path("/articulospedido")
public class ArticulosPedidoRestServlet {
    @EJB
    private ArticulosPedidoService service;

    private SimpleDateFormat df;
    private ObjectMapper mapper; // Transforma JSON en Objetos y viceversa.

    public ArticulosPedidoRestServlet() {
        mapper = new ObjectMapper();

        // Le provee el formateador de fechas.
        df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAllArticulosPedido(@DefaultValue("1") @QueryParam("page") Integer page,
    @DefaultValue("100") @QueryParam("cant") Integer cant) {
        
        /*Aquí se define la cantidad que se va a mostrar por página.
        El motivo para que sean 100 los resultados a mostrar es por el test, ya que si se muestran por página, el step
        de la línea 145 de remitos_steps.js falla.
        */

        ResultsPage<ArticulosPedido> resultsPage = service.findByPage(page, cant);

        String data;

        Collection<ArticulosPedido> results = resultsPage.getResults().stream().map(ap -> {
            ap.getPedido().setArticulosPedido(null);
            return ap;
        }).collect(Collectors.toList());

        resultsPage.setResults(results);
        
        try {
            data = mapper.writeValueAsString(resultsPage);
        } catch (IOException e) {
            return ResponseMessage.message(501, "Formato incorrecto en datos de entrada", e.getMessage());
        }

        return ResponseMessage.message(200, "Articulos pedido recuperados con éxito", data);
    }

    @GET
    @Path("/sinremitohasta/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findBeforeDateNotInRemito(@PathParam("date") String date) throws IOException {
        Date cal;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal = sdf.parse(date);
        } catch (Exception e) {
            return ResponseMessage.message(501, "No se pudo dar formato a la fecha", e.getMessage());
        }

        Collection<ArticulosPedido> articulosPedido = service.findSinRemitoHasta(cal);
        String response;
        if (articulosPedido != null) {
            Collection<ArticulosPedido> results = articulosPedido.stream().map(ap -> {
                ap.getPedido().setArticulosPedido(null);
                return ap;
            }).collect(Collectors.toList());

            try {
                response = mapper.writeValueAsString(results);
            } catch (JsonProcessingException e) {
                return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
            }

            return ResponseMessage.message(200, "ArticulosPedido recuperados exitosamente", response);
        } else
            return ResponseMessage.message(500, "No se pudieron recuperar los artículos pedidos");
    }

    @GET
    @Path("/sinfacturar")
    @Produces(MediaType.APPLICATION_JSON)
    public String findNotInFactura() throws IOException {

        Collection<ArticulosPedido> articulosPedido = service.findSinFacturar();
        String response;
        if (articulosPedido != null) {
            Collection<ArticulosPedido> results = articulosPedido.stream().map(ap -> {
                ap.getPedido().setArticulosPedido(null);
                return ap;
            }).collect(Collectors.toList());

            try {
                response = mapper.writeValueAsString(results);
            } catch (JsonProcessingException e) {
                return ResponseMessage.message(502, "No se pudo dar formato a la salida", e.getMessage());
            }

            return ResponseMessage.message(200, "ArticulosPedido recuperados exitosamente", response);
        } else
            return ResponseMessage.message(500, "No se pudieron recuperar los artículos pedidos");
    }
}
