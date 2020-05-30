package utils;

import base.BaseTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
	
	static String projectPath=new File("").getAbsolutePath();

    public static String getConfigProperties(String sKey) {
        Properties prop;
        String sValue = null;
        try {
        	
        	
            InputStream input = new FileInputStream(projectPath+BaseTest.CONFIG_FILE_PATH);
            prop = new Properties();
            // load a properties file
            prop.load(input);
            sValue = prop.getProperty(sKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sValue;
    }

    public static String getConfigiOSAndroidProperties(String sKey) {
        Properties iOSProp;
        String sValue = null;
        try {
        	
        	
            InputStream iOSinput = new FileInputStream(projectPath+BaseTest.IOS_CONFIG_FILE_PATH);
            iOSProp = new Properties();
            // load a properties file
            iOSProp.load(iOSinput);
            sValue = iOSProp.getProperty(sKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sValue;
    }
    
    
}
