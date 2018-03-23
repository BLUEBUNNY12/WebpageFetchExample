/*The purpose of this program is to explore 
 * and demonstrate how fetching a webpage works
*/


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class Fetch {
	public static void main(String[] args) throws Exception {

		final int portNumber = 80;

		Scanner input = new Scanner(System.in);

		System.out.println("Enter a full website to fetch it, like this one with a cute cat picture: https://www.pets4homes.co.uk/images/classifieds/2014/05/02/622473/large/silver-tabby-and-brown-tabby-maine-coon-kittens-5365125f71e20.jpg");
		System.out.println("");
		String webpage = input.nextLine();

		//fetch url
		URL url = new URL(webpage);


		System.out.println("protocol = " + url.getProtocol());
		System.out.println("filename = " + url.getFile());
		System.out.println("host = "  + url.getHost());

		String localFileName="";
		if(webpage.endsWith("/")){
			localFileName = "index.html";
		}
		else{
			// everything in webpage after the last '/'
			localFileName = webpage.substring(webpage.lastIndexOf('/')+1);
		}
		File f = new File(localFileName);
		f.createNewFile();
		FileOutputStream toFile = new FileOutputStream(f);
		

		System.out.println("Output will be saved as " + localFileName);


		Socket socket = new Socket(url.getHost(), portNumber);
		OutputStream os = socket.getOutputStream();


		StringBuilder sb = new StringBuilder();
		sb.append("GET ");
		sb.append(url.getFile());
		sb.append(" HTTP/1.1 \r\n"); /*** this should come from URL ***/
		sb.append("Host: ");
		sb.append(url.getHost());
		sb.append(" host \r\n"); /*** same as url.getHost() above ***/
		sb.append("Connection: close \r\n\r\n");

		os.write(sb.toString().getBytes());

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int ch;
		while((ch = socket.getInputStream().read()) != -1){
			out.write(ch);
		}

		byte[] response = out.toByteArray();
		String test = new String(response);
		int index = test.indexOf("\r\n\r\n");
		String header = new String(response, 0, index);
		System.out.println(header);

		while(response[index] == '\r' || response[index] == '\n'){
			index++;
		}

		toFile.write(response, index, (response.length - index));

		socket.close();
		input.close();
	}
}
