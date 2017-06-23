package racingHorse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HangangTempGetter {
//	�Ѱ� ������ �޾ƿ��� �ý���
//	�ݵ�� json-simple-1.1.1.jar �� ������Ʈ > Build Path > Add External Library �� �߰��ϵ��� �Ѵ�.
	public static double getHangangTemp(){
		try {
			URL url = new URL("http://hangang.dkserver.wo.tc/");
			HttpURLConnection conn =(HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setReadTimeout(20000);
			conn.setRequestMethod("POST");		// or "GET"
			
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			for(;;){
				String line = br.readLine();
				if(line == null) break;
				sb.append(line + "\n");
			}
			
			br.close();
			conn.disconnect();
			
			String hangangJsonData = sb.toString();
//			System.out.println(sb.toString());
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(hangangJsonData);
			double temp = Double.parseDouble(jsonObject.get("temp").toString());
			
//			System.out.println("�Ѱ� ������ : " + temp + "��");
			return temp;
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(NumberFormatException e1){
			e1.printStackTrace();
		}
		return 0;
	}
	
	public static String getHangangTime(){
		try {
			URL url = new URL("http://hangang.dkserver.wo.tc/");
			HttpURLConnection conn =(HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setReadTimeout(20000);
			conn.setRequestMethod("POST");		// or "GET"
			
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			for(;;){
				String line = br.readLine();
				if(line == null) break;
				sb.append(line + "\n");
			}
			
			br.close();
			conn.disconnect();
			
			String hangangJsonData = sb.toString();
//			System.out.println(sb.toString());
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(hangangJsonData);
			String temp = jsonObject.get("time").toString();
			
//			System.out.println("�Ѱ� ������ : " + temp + "��");
			return temp;
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(NumberFormatException e1){
			e1.printStackTrace();
		}
		return "";
	}

}
