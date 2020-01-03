import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.*;
 
public class Client 
{ 
    public static void main(String[] args) 
    {
        String hostname = "192.168.43.70";
        int port = 12348;
 
        try (Socket socket = new Socket(hostname, port)) 
        {
            // Send JSON data
            JSONObject json = new JSONObject();
            json.put("operation", "sensor");
            json.put("device_id", "123");
            String jsonData = json.toString();

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 

            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData)); 
        } 
        catch (JSONException ex)
        {
            System.out.println("JSON error: " + ex.getMessage());   
        }
        catch (UnknownHostException ex) 
        {
            System.out.println("Server not found: " + ex.getMessage());
 
        } 
        catch (IOException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
