package com.quiz.web.MercQuizApp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.quiz.web.MercQuizApp.service.dao.QuizDAO;
import com.quiz.web.MercQuizApp.service.utils.AnswerTO;
import com.quiz.web.MercQuizApp.service.utils.QuizTO;
import com.quiz.web.MercQuizApp.service.utils.QuizUtil;


@RestController

public class QuizService {
	@Autowired
	QuizDAO dao;
	
	@Autowired
	QuizUtil util;
	
	@RequestMapping("/quizList")
	public String getQuizList()
	{
		JSONArray q = dao.retrieveQuizList();
		return q.toString();
	}
	
	@RequestMapping(value = "/retrieveQuiz", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String retrieveQuiz(@RequestBody QuizTO quizTO)
	{
		String q = dao.retrieveQuiz(quizTO);
		return q;
	}
	
	@RequestMapping(value = "/deleteAnswer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteAnswer(@RequestBody AnswerTO answerTO)
	{
		dao.deleteAnswer(answerTO);
		return util.successObject().toString();
	}
	
	@RequestMapping(value = "/deleteQuiz", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteQuiz(@RequestBody QuizTO quizTO)
	{
		dao.deleteQuiz(quizTO);
		JSONArray q = dao.retrieveQuizList();
		return q.toString();
	}

	@RequestMapping(value = "/retrieveAnswer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String retrieveAnswer(@RequestBody AnswerTO answerTO)
	{
		String q  = dao.retrieveAnswer(answerTO);
		return q;
	}
	
	@RequestMapping(value = "/retrieveAnswerList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String retrieveAnswerList(@RequestBody AnswerTO answerTO)
	{
		JSONArray a = dao.retrieveAnswerList(answerTO);
		return a.toString();
	}

	@RequestMapping(value = "/saveQuiz", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveQuiz(@RequestBody Map<String, Object> quiz)
	{
		JSONObject obj = new JSONObject(quiz);
		dao.saveQuiz(obj);
		return util.successObject().toString();
	}
	
	@RequestMapping(value = "/saveQuizAnswers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String saveQuizAnswers(@RequestBody Map<String, Object> q)
	{
		JSONObject obj = new JSONObject(q);
		dao.saveQuizAnswers(obj);
		return util.successObject().toString();
	}
	
	@RequestMapping(value = "/updateQuizAnswers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateQuizAnswers(@RequestBody Map<String, Object> q)
	{
		JSONObject obj = new JSONObject(q);
		dao.updateAnswer(obj);
		return util.successObject().toString();
	}
	
	@RequestMapping(value = "/editQuiz", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String editQuiz(@RequestBody Map<String, Object> quiz)
	{
		JSONObject obj = new JSONObject(quiz);
		dao.editQuiz(obj);
		return util.successObject().toString();
	}

	
}
