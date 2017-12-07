package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import org.json.JSONArray;

public class MyThread extends Thread {
	BufferedReader inFromClient;
	DataOutputStream outToClient;
	int id;
	static Boolean first;

	public MyThread(BufferedReader inFromClient, DataOutputStream outToClient, int id) {
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.id = id;
		first = true;
		System.out.println("Thread # " + id + " created");
	}

	@Override
	public void run() {
		String clientSentence;
		String outSentence;
		
		try {
			String temp = inFromClient.readLine();
			System.out.println("first query " + temp);
			while (!temp.equals("CLOSE")) {
				System.out.println("readline oncesi");
				if (isFirst()) {
					clientSentence = temp;
					first = false;
				} else {
					System.out.println("readline oncesi oncesi");
					while (!inFromClient.ready())
						;
					clientSentence = inFromClient.readLine();
					System.out.println("diger queryler " + clientSentence);
					if (clientSentence != null)
						if (clientSentence.equals("CLOSE"))
							break;
				}
				System.out.println("Line read");

				if (clientSentence.contains("INSERT") || clientSentence.contains("UPDATE")
						|| clientSentence.contains("DELETE")) {
					Server.rw.lockWrite();
					Server.connectDB(clientSentence);
					Server.rw.unlockWrite();
				} else {
					Server.rw.lockRead();
					JSONArray ja = Server.connectDB(clientSentence);
					Server.rw.unlockRead();
					System.out.println(clientSentence);
					outSentence = ja.toString() + "\n";
					System.out.println(outSentence);

					outToClient.writeBytes(outSentence);
					outToClient.flush();
					System.out.println("inner while sonu");
				}
			}
			first = true;
			System.out.println("outer while sonu");
		} catch (Exception e) {
			System.out.println("babalara geldik id: " + id);
			e.printStackTrace();
		}
		System.out.println("Thread id : " + id + " isi bitti");
	}

	public static boolean isFirst() {
		return first;
	}
}
