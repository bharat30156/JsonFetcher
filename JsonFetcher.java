import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 *
 * @author Bharat
 * This code can be used to get HTML of a URL as well. Just edit it as per convenience 
 *
 */
public class JsonFetcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonFetcher.class);
	
	public static JSONObject urlToJson(URL urlString) throws JSONException
	{
		StringBuilder sb = null;
		URL url;
		URLConnection urlCon;
		try
		{
			url = urlString;
			urlCon = url.openConnection();
			BufferedReader in = null;
			if(urlCon.getHeaderField("Content-Encoding") != null && urlCon.getHeaderField("Content-Encoding").equals("gzip"))
			{
				LOGGER.info("Reading data form URL as GZIP Stream");
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(urlCon.getInputStream())));
			}
			else
			{
				LOGGER.info("Reading data from URL as InputStream");
				in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			}
			String inputLine;
			sb = new StringBuilder();
			
			while((inputLine = in.readLine()) != null)
			{
				sb.append(inputLine);
			}
			in.close();
		}
		catch(IOException e)
		{
			LOGGER.info("Exception while JSON form URL - {}", e);
		}
		if(sb != null)
		{
			return new JSONObject(sb.toString());
		}
		else
		{
			LOGGER.warn("NO Json Found in given URL");
			return new JSONObject("");
		}
	}
}
