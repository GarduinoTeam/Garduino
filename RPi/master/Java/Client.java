import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.*;
 
public class Client 
{ 
    private static String hostname = "192.168.43.70";
    private static int port = 12345;

    public static void main(String[] args) 
    {
        int argsLength = args.length;
        int rc = 0;

        if(argsLength == 2)
        {
            rc = sendRequest(args[0], args[1], 0);
        }
        else if(argsLength == 3)
        {
            rc = sendRequest(args[0], args[1], Integer.valueOf(args[2]));   
        }
        else{
            System.exit(0);
        }
    }

    private static int sendRequest(String operation, String device_id, int irrigation_time)
    {
        // Send petition
        try (Socket socket = new Socket(hostname, port)) 
        {
            JSONObject json = new JSONObject();

            if(operation != ""){
                json.put("operation", operation);
            }
            else{
                return -1;
            }

            if(device_id != ""){
                json.put("device_id", device_id);
            }
            else{
                return -1;
            }
            
            if(irrigation_time != 0){
                json.put("irrigation_time", irrigation_time);
            }
        
            String jsonData = json.toString();

            // Send JSON data
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            
            // First send JSON data length
            System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 

            // Send the JSON
            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData)); 
        }
        catch (JSONException ex) 
        {
            System.out.println("Invalid JSON error: " + ex.getMessage());
            return -1;
        }
        catch (UnknownHostException ex) 
        {
            System.out.println("Server not found: " + ex.getMessage());
            return -1;
        } 
        catch (IOException ex) 
        {
            System.out.println("I/O error: " + ex.getMessage());
            return -1;
        }   

        return 0;
    }
}