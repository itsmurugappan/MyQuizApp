package com.quiz.web.MercQuizApp.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

@Service ("quizUtil")
public class QuizUtil {
	
	public JSONObject requestParamsToJSON(ServletRequest req) {
		JSONObject obj = null;
		  try{
		BufferedReader reader = req.getReader();
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while( line != null ){
			sb.append( line );
			line = reader.readLine();
		}
		System.out.println(sb.toString());
		obj = new JSONObject(sb.toString());
		  }catch(Exception ex)
		  {
			  //handle
		  }
		  return obj;
		}



public String from(InputStream is) throws IOException {
    if (is == null)
        return "";
    StringBuilder sb = new StringBuilder();
    try {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StringUtils.UTF8));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
    } finally {
        is.close();
    }
    return sb.toString();
}

	public JSONObject successObject()
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", "200");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}