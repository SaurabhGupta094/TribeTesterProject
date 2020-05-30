package utils;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class LiveResultListener extends TestListenerAdapter implements ISuiteListener {

    String sJsObjectPath = System.getProperty("user.dir") + File.separator + "LiveResult" + File.separator + "jsonData.js";
    String sJsonData;
    private long testCount = 0;

    private long testStartTime;
    private long testEndTime;
    private List<ITestNGMethod> testMethods = null;

    public void onStart(ISuite suite) {
        try {
            createMetaData(suite);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    public void createMetaData(ISuite suite) throws UnknownHostException {

        JSONObject metaData = new JSONObject();
        metaData.put("User", InetAddress.getLocalHost().getHostName());
        metaData.put("OS", System.getProperty("os.name"));
        metaData.put("TotalTest", suite.getAllMethods().size());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("metadata", metaData);
        jsonObject.put("Result", new JSONArray());

        writeToFile(sJsObjectPath, jsonObject);


        //Read file
//        String sFileContent=readFile();

//        remove var
//        String sJsonContent=sFileContent.replace("var data=","");

//        string to jsonObject
//        stringToJSONObject(sJsonContent);
    }


    public JSONObject stringToJSONObject(String sStringJson) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(sStringJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void onFinish(ISuite suite) {

    }

    public void writeToFile(String sPath, JSONObject jsonObject) {
        FileWriter file = null;
        try {
            file = new FileWriter(sPath);
            file.write("var data=" + jsonObject.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("JSON Object: " + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void onTestStart(ITestResult result) {

        //Adding Test
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Suite", result.getTestContext().getSuite().getName());
        jsonObject.put("Test", result.getTestContext().getName());
        jsonObject.put("TestName", result.getMethod().getMethodName());
        jsonObject.put("Result", "Started");
        jsonObject.put("Error", "---");
        jsonObject.put("TimeDuration", -1);

        testStartTime = System.currentTimeMillis();

        addResult(jsonObject);
    }

    public String readFile(String sJsObjectFilePath) {
        File file = new File(sJsObjectFilePath);
        String content = "";
        try {
            // Read the entire contents of sample.txt
            content = FileUtils.readFileToString(file, "UTF-8");
            System.out.println("File content: " + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    public void addResult(JSONObject jsObjectResult) {

        //Read file to string, remove var, and convert to JsonObject
        String sFileContent = readFile(sJsObjectPath);
        String sJsonContent = sFileContent.replace("var data=", "");
        JSONObject jsonObject = stringToJSONObject(sJsonContent);

        try {
            JSONArray result = (JSONArray) jsonObject.get("Result");
            result.add(jsObjectResult);
            jsonObject.put("Result", result);
            writeToFile(sJsObjectPath, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateResult(JSONObject jsObjectResult) {

        //Read file to string, remove var, and convert to JsonObject
        String sFileContent = readFile(sJsObjectPath);
        String sJsonContent = sFileContent.replace("var data=", "");
        JSONObject jsonObject = stringToJSONObject(sJsonContent);

        JSONArray result = (JSONArray) jsonObject.get("Result");
        for (int i = 0; i < result.size(); i++) {
            String sSuiteName = ((JSONObject) result.get(i)).get("Suite").toString();
            String sTest = ((JSONObject) result.get(i)).get("Test").toString();
            String sTestName = ((JSONObject) result.get(i)).get("TestName").toString();
            if (sSuiteName.contentEquals(jsObjectResult.get("Suite").toString()) && sTestName.contentEquals(jsObjectResult.get("TestName").toString()) && sTest.contentEquals(jsObjectResult.get("Test").toString())) {
                ((JSONObject) result.get(i)).put("Error", jsObjectResult.get("Error") + "");
                ((JSONObject) result.get(i)).put("Result", jsObjectResult.get("Result").toString());
                ((JSONObject) result.get(i)).put("TimeDuration", jsObjectResult.get("TimeDuration").toString());
            }
        }

        jsonObject.put("Result", result);
        writeToFile(sJsObjectPath, jsonObject);


    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        System.out.println("onTestSuccess");

        testEndTime = System.currentTimeMillis();

        //Updating LiveResult with Pass
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Suite", result.getTestContext().getSuite().getName());
        jsonObject.put("Test", result.getTestContext().getName());
        jsonObject.put("TestName", result.getMethod().getMethodName());
        jsonObject.put("Result", "Pass");
        jsonObject.put("Error", "---");
        jsonObject.put("TimeDuration", millisToSecond());

        updateResult(jsonObject);


    }

    public String millisToSecond() {
        String durationInSeconds = Long.toString((testEndTime - testStartTime) / 1000);
        return durationInSeconds;
    }

    /**
     * @param result
     */
    @Override
    public synchronized void onTestFailure(ITestResult result) {

        System.out.println("*********Test Failed*********");
        testEndTime = System.currentTimeMillis();
        //Updating LiveResult with Pass
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Suite", result.getTestContext().getSuite().getName());
        jsonObject.put("Test", result.getTestContext().getName());
        jsonObject.put("TestName", result.getMethod().getMethodName());
        jsonObject.put("Result", "Fail");
        jsonObject.put("Error", result.getThrowable().getMessage());
        jsonObject.put("TimeDuration", millisToSecond());

        updateResult(jsonObject);


    }


}
