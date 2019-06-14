import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class Http_Proxy extends Backend_Proxy{

	private String USER_AGENT = "Mozilla/5.0";
	private String url;
	private HttpClient client;
	
	
	
	public Http_Proxy (String from, String to, String server_url) {
		super(from, to);
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		client = new DefaultHttpClient(httpParams);
		url = server_url;
	}
	
	public String get_from() {
		return sender;
	}
	
	public String get_to() {
		return receiver;
	}
	
	public String send (String message) throws Exception {
		String s = url;
		HttpPut put = new HttpPut(s);

		try {

			// add header
			put.setHeader("User-Agent", USER_AGENT);
	
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("officeName", receiver));
			urlParameters.add(new BasicNameValuePair("message", message));
	
	
			put.setEntity(new UrlEncodedFormEntity(urlParameters));
	
			HttpResponse response = client.execute(put);
			System.out.println("\nSending 'PUT' request to URL : " + s);
			System.out.println("Post parameters : " + put.getEntity());
			System.out.println("Response Code : " + 
	                                    response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			EntityUtils.consume(response.getEntity());
	
			System.out.println(result.toString());
//			put.releaseConnection();
			
			return result.toString();
		} catch (ConnectTimeoutException e) {
//			put.releaseConnection();
			throw e;
		}
	}
	
	
	public String receive() throws Exception {
		HttpGet request = new HttpGet(url + "?officeName=" + receiver);
		try {
			
			
	
			// add request header
			request.addHeader("User-Agent", USER_AGENT);
			
	
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + url + "/"+ receiver + "/" + sender);
			System.out.println("Response Code : " + 
	                       response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(
	                       new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			EntityUtils.consume(response.getEntity());
			
//			request.releaseConnection();
			return result.toString();
			
		} catch (ConnectTimeoutException e) {
//			request.releaseConnection();
			throw e;
		}
		
	}

	@Override
	String test_session(String[] test_parameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	String create_session(String[] session_parameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	String start_session() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
}


