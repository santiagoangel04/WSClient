package com.desaextremo.wsclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Consume servicio Web con tipo de peticion get:
 * https://g204ecfa60e021a-g3qlhq6a8n5r6dq0.adb.us-phoenix-1.oraclecloudapps.com/ords/admin/computer/computer
 * Obtener respuesta del servicio: Para esto hce uso del API httpClient para
 * conectarse al servicio y obtener los datos Convertir la respuesta a formato
 * JSON: Usa el api json-simple para obtener la respuesta del servicio web,
 * acceder a los elementos de tipo JSON, e imprimir los valores.
 *
 * @author desaextremo
 */
public class WSClient {
    
    public static void main(String[] args) throws ParseException {
        StringBuilder salida = new StringBuilder();
        try {
            
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    "https://g204ecfa60e021a-g3qlhq6a8n5r6dq0.adb.us-phoenix-1.oraclecloudapps.com/ords/admin/computer/computer");
            getRequest.addHeader("accept", "application/json");
            
            HttpResponse response = httpClient.execute(getRequest);
            
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(br);

            // get an array from the JSON object
            JSONArray item = (JSONArray) jsonObject.get("items");
            
            salida.append("Datos sin formatear");
            // take the elements of the json array
            for (int i = 0; i < item.size(); i++) {
                salida.append("El elemento : " + i + " del arreglo: " + item.get(i)).append("\n");
            }
            Iterator i = item.iterator();
            
            salida.append("Datos individuales y formateados").append("\n");
            // take each value from the json array separately
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                salida.append("id :" + innerObj.get("id") + " brand :" + innerObj.get("brand") + " model :" + innerObj.get("model") + " category_id :" + innerObj.get("category_id") + " name :" + innerObj.get("name"));
                salida.append("\n");                
            }

            //cierra conexion
            httpClient.getConnectionManager().shutdown();

            //
            JOptionPane.showMessageDialog(null, salida.toString(), "Listado de Computadores", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (ClientProtocolException e) {
            
            e.printStackTrace();
            
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        
    }
}
