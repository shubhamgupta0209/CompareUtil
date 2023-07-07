package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ReadProperties {
	
	public static HashMap<String, String> getProperties() {
		
		HashMap<String, String> props = new HashMap();
		try (InputStream input = new FileInputStream("./src/test/resources/properties/config.properties")) {

	        Properties prop = new Properties();
	        prop.load(input);

	        prop.forEach((key, value) -> props.put(String.valueOf(key), String.valueOf(value)));

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
		return props;
	}
	
	

}
