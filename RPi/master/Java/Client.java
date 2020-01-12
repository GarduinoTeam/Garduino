import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.*;
import java.io.*;
 
public class Client 
{
    private static String hostname = "192.168.43.70";
    private static int port = 12346;

    public static void main(String[] args) throws InterruptedException, JSONException, UnknownHostException, IOException
    {
        int argsLength = args.length;

        try (Socket socket = new Socket(hostname, port)) 
        {            
            if(argsLength == 2) // stop_irrigate, create_device, delete_device, list_device, sensor, webcam
            {
                System.out.println("Introduced : " + args[0] + " and " + args[1]);
                sendRequest(socket, args[0], args[1], 0);
            }
            else if(argsLength == 3) // Irrigate
            {
                System.out.println("Introduced : " + args[0] + " and " + args[1] + " and " + args[2]);
                sendRequest(socket, args[0], args[1], Integer.valueOf(args[2]));   
            }
            else if(argsLength == 7) // Create rule_condition
            {
                createRuleCondition(socket, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            }
            else if(argsLength == 10) // Create rule_time_condition
            {
                createRuleTimeCondition(socket, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            }
            else{
                System.out.println("Invalid number of paramaters");
                System.exit(0);
            }
        }
    }

    private static void sendRequest(Socket socket, String operation, String device_id, int irrigation_time)
    throws InterruptedException, JSONException, UnknownHostException, IOException
    {
        JSONObject json = new JSONObject();

        if(operation != "") json.put("operation", operation); else System.exit(-1);

        if(device_id != "") json.put("device_id", device_id); else System.exit(-1);
        
        if(irrigation_time != 0) json.put("irrigation_time", irrigation_time);
    
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

    private static void createRuleCondition( Socket socket, String operation, String device_id, String rule_type, 
                                            String rule_id, String rule_condition_id, String value, String rule_condition_type)
    throws InterruptedException, JSONException, UnknownHostException, IOException
    {
        JSONObject json = new JSONObject();

        json.put("operation", operation);
        json.put("device_id", device_id);
        json.put("rule_type", rule_type);
        json.put("rule_id", rule_id);
        json.put("rule_condition_id", rule_condition_id);
        json.put("value", value);
        json.put("rule_condition_type", rule_condition_type);

    
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

    private static void createRuleTimeCondition( Socket socket, String operation, String device_id, String rule_type, String rule_id, 
            String rule_time_condition_id, String start_time, String end_time, String weeks, String months, String specific_dates)
    throws InterruptedException, JSONException, UnknownHostException, IOException
    {
        JSONObject json = new JSONObject();

        json.put("operation", operation);
        json.put("device_id", device_id);
        json.put("rule_type", rule_type);
        json.put("rule_id", rule_id);
        json.put("rule_time_condition_id", rule_time_condition_id);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        json.put("weeks", weeks);
        json.put("months", months);
        json.put("specific_dates", specific_dates);

    
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
}