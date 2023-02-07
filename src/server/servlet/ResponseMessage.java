package servlet;

public class ResponseMessage {

  static public String message(int code, String text) {
    return "{\"StatusCode\":" + code +", \"StatusText\":\"" + text + "\"}";
  }


  static public String message(int code, String text, String json) {

    // Ejercicio, verificar que sea un json bien formado.

    return "{\"StatusCode\":" + code + 
      ", \"StatusText\":\"" + text + "\"" +
      ", \"data\":" + json + 
      "}";
  }

}