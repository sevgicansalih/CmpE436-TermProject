package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	
	/*
	 * Test code for client server connection and database queries.
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String sentence;
		String modifiedSentence;
		JSONArray temp;
		JSONObject result;
		try {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

			Socket clientSocket = new Socket("79.123.176.48", 50000);

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sentence = inFromUser.readLine();

			outToServer.writeBytes(sentence + "\n");

			System.out.println("burada beklemem lazim aslinda " + inFromServer.ready());


			modifiedSentence = inFromServer.readLine();
			
			temp = new JSONArray(modifiedSentence);
			System.out.println(temp.toString());
			result = temp.getJSONObject(0);
			
			//System.out.println("object ==> "+result.toString());
			System.out.println("From Server : "+result.getString("password").equals("admin"));

			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
