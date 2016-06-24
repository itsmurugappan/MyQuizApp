package com.quiz.web.MercQuizApp.service.dao;


import static com.quiz.web.MercQuizApp.service.utils.AWSResources.A_PATH;
import static com.quiz.web.MercQuizApp.service.utils.AWSResources.DYNAMODB_MAPPER;
import static com.quiz.web.MercQuizApp.service.utils.AWSResources.Q_PATH;
import static com.quiz.web.MercQuizApp.service.utils.AWSResources.S3;
import static com.quiz.web.MercQuizApp.service.utils.AWSResources.S3_BUCKET_NAME;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.quiz.web.MercQuizApp.service.utils.AnswerTO;
import com.quiz.web.MercQuizApp.service.utils.Questions;
import com.quiz.web.MercQuizApp.service.utils.QuizAnswers;
import com.quiz.web.MercQuizApp.service.utils.QuizTO;
import com.quiz.web.MercQuizApp.service.utils.QuizUtil;

@Repository("quizDao")
public class QuizDAO {


	@Autowired
	QuizUtil util ;
	
	@Autowired
	ApplicationContext appCtx;
	
	//save quiz
	public void saveQuiz (JSONObject q)
	{
		try{
				Questions quiz = (Questions) appCtx.getBean(Questions.class);
				quiz.setQuestionsLink(DYNAMODB_MAPPER.createS3Link(S3_BUCKET_NAME, Q_PATH + q.getJSONObject("quiz").getString("name") + ".json"));
				quiz.setCreationTime(new Date());
				quiz.setQuizName(q.getJSONObject("quiz").getString("name"));
				quiz.getQuestionsLink().uploadFrom(q.toString().getBytes());
				DYNAMODB_MAPPER.save(quiz);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public JSONArray retrieveQuizList()
	{
		try
		{
	        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
	        List<Questions> q = DYNAMODB_MAPPER.scan(Questions.class, scanExpression);
	        JSONArray arr = new JSONArray();
	        //JSONObject retObj = new JSONObject();
	        q.stream()
	        .forEach((qs) -> 
	        {
	        	
	        	JSONObject obj = new JSONObject();
	        	try {
					obj.put("name", qs.getQuizName());
					obj.put("id", qs.getId());
					obj.put("createdDate", qs.getCreationTime());
					arr.put(obj);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        });
	        //retObj.put("quizs", arr);
	        return arr;
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}


public String retrieveQuiz(QuizTO quizTO)
{
	try
	{
		String id = quizTO.getQuizId();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("id", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(id)));
        List<Questions> q = DYNAMODB_MAPPER.scan(Questions.class, scanExpression);
    	String obj = util.from(S3.getObject(S3_BUCKET_NAME, q.get(0).getQuestionsLink().getKey()).getObjectContent());
        return obj;
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return null;
}

public void deleteQuiz(QuizTO quizTO)
{
	try
	{
		String id = quizTO.getQuizId();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("id", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(id)));
        List<Questions> q = DYNAMODB_MAPPER.scan(Questions.class, scanExpression);
        //delete questions json
    	S3.deleteObject(S3_BUCKET_NAME, q.get(0).getQuestionsLink().getKey());
    	//delete answers
    	List<QuizAnswers> answers = retrieveAnswersbyName(q.get(0).getQuizName());
    	answers.stream()
    	.forEach((a) -> {
    		S3.deleteObject(S3_BUCKET_NAME, a.getAnswersLink().getKey());
    		DYNAMODB_MAPPER.delete(a);
    	});  
    	// delete quiz
    	DYNAMODB_MAPPER.delete(q.get(0));
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
}


public void saveQuizAnswers (JSONObject answer)
{
	
	try{
			QuizAnswers quizA = (QuizAnswers) appCtx.getBean(QuizAnswers.class);
			quizA.setAnswersLink(DYNAMODB_MAPPER.createS3Link(S3_BUCKET_NAME, A_PATH + answer.getJSONObject("q").getString("name") + "_" + 
					answer.getJSONObject("q").getString("attendee") + ".json"));
			quizA.setCreationTime(new Date());
			quizA.setQuizName(answer.getJSONObject("q").getString("name"));
			quizA.getAnswersLink().uploadFrom(answer.toString().getBytes());
			DYNAMODB_MAPPER.save(quizA);
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

public List<QuizAnswers> retrieveAnswersbyName(String name)
{
	try
	{
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("quizName", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(name)));
        List<QuizAnswers> a = DYNAMODB_MAPPER.scan(QuizAnswers.class, scanExpression);
        return a;
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return null;
}

public JSONArray retrieveAnswerList(AnswerTO answerTO)
{
	try
	{
        List<QuizAnswers> a =retrieveAnswersbyName(answerTO.getName());
        JSONArray arr = new JSONArray();
        a.stream()
        .forEach((as) -> 
        {
        	
        	JSONObject obj = new JSONObject();
        	try {
        		JSONObject	aRecord = new JSONObject(util.from(S3.getObject(S3_BUCKET_NAME, as.getAnswersLink().getKey()).getObjectContent()));
        		obj.put("id",as.getId());
        		obj.put("date",as.getCreationTime());
        		obj.put("answer", aRecord);
				arr.put(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        return arr;
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return null;
}

public String retrieveAnswer(AnswerTO answerTO)
{
	try
	{
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("id", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(answerTO.getId())));
        List<QuizAnswers> a = DYNAMODB_MAPPER.scan(QuizAnswers.class, scanExpression);
        JSONArray arr = new JSONArray();
        	String obj = null;
        	try {
        		obj = util.from(S3.getObject(S3_BUCKET_NAME, a.get(0).getAnswersLink().getKey()).getObjectContent());
				arr.put(obj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return obj;
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return null;
}

public void deleteAnswer(AnswerTO answerTO)
{
	try
	{
		String id = answerTO.getId();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("id", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(id)));
        List<QuizAnswers> a = DYNAMODB_MAPPER.scan(QuizAnswers.class, scanExpression);
        //delete answer json
    	S3.deleteObject(S3_BUCKET_NAME, a.get(0).getAnswersLink().getKey());
    	// delete quiz
    	DYNAMODB_MAPPER.delete(a.get(0));
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

public void updateAnswer(JSONObject obj)
{
	try
	{
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("quizName", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(obj.getJSONObject("q").getString("name"))));
        List<QuizAnswers> a = DYNAMODB_MAPPER.scan(QuizAnswers.class, scanExpression);
        	try {
				a.get(0).getAnswersLink().uploadFrom(obj.toString().getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

public void editQuiz(JSONObject q)
{
	try
	{
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("quizName", new Condition()
        .withComparisonOperator(ComparisonOperator.EQ)
        .withAttributeValueList(new AttributeValue(q.getJSONObject("quiz").getString("name"))));
        List<Questions> quiz = DYNAMODB_MAPPER.scan(Questions.class, scanExpression);
        	try {
				quiz.get(0).getQuestionsLink().uploadFrom(q.toString().getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

}

