import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.*;
 
public class Client 
{ 
    private static String hostname = "192.168.43.70";
    private static int port = 12346;

    public static void main(String[] args) 
    {
        int argsLength = args.length;
        int rc = 0;

        if(argsLength == 2) // stop_irrigate, create_device, delete_device, list_device, sensor, webcam
        {
            System.out.println("Introduced : " + args[0] + " and " + args[1]);
            rc = sendRequest(args[0], args[1], 0);
        }
        else if(argsLength == 3) // Irrigate
        {
            System.out.println("Introduced : " + args[0] + " and " + args[1] + " and " + args[2]);
            rc = sendRequest(args[0], args[1], Integer.valueOf(args[2]));   
        }
        else{
            System.out.println("Invalid number of paramaters");
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
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // -------------- Send REQUEST --------------
            // First send JSON data length
            System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 

            Thread.sleep(200);

            // Send the JSON
            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData)); 
   
            // -------------- Receive data --------------
            String line;
            System.out.println("Received: " + reader.readLine());


        }
        catch (InterruptedException ex) 
        {
            System.out.println("Thread exception error: " + ex.getMessage());
            return -1;
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

    private static int createRuleCondition(String device_id, String rule_type, String rule_id, String rule_condition_id, String value, String type)
    {
        // Send petition
        try (Socket socket = new Socket(hostname, port)) 
        {
            JSONObject json = new JSONObject();

            json.put("operation", "create_rule");
            json.put("rule_type", rule_type);
            json.put("rule_id", rule_id);
            json.put("rule_condition_id", rule_condition_id);
            json.put("value", value);
            json.put("type", type);

        
            String jsonData = json.toString();
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // -------------- Send REQUEST --------------
            // First send JSON data length
            System.out.println(jsonData.length());
            writer.println(String.valueOf(jsonData.length())); 

            Thread.sleep(200);

            // Send the JSON
            System.out.println(jsonData);
            writer.println(String.valueOf(jsonData)); 
        }
        catch (InterruptedException ex) 
        {
            System.out.println("Thread exception error: " + ex.getMessage());
            return -1;
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
