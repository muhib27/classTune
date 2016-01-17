package com.classtune.app.schoolapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssessmentQuestion {
	
	
	@SerializedName("id")
	private String id;
	
	@SerializedName("question")
	private String question;
	
	@SerializedName("explanation")
	private String explanation;

	@SerializedName("mark")
	private String mark;
	
	@SerializedName("time")
	private String time;

	@SerializedName("style")
	private String style;
	
	@SerializedName("created_date")
	private String createdDate;
	
	@SerializedName("option")
	private List<Option> listQuestion;
	
	public AssessmentQuestion()
	{
		
	}
	
	
	public AssessmentQuestion(String id, String question, String explanation, String mark, String style, String createdDate, List<Option> listQuestion)
	{
		this.id = id;
		this.question = question;
		this.explanation = explanation;
		this.mark = mark;
		this.style = style;
		this.createdDate = createdDate;
		this.listQuestion = listQuestion;
	}
	
	
	public AssessmentQuestion(String question, String explanation, String mark, String style, String createdDate, List<Option> listQuestion)
	{
		this.question = question;
		this.explanation = explanation;
		this.mark = mark;
		this.style = style;
		this.createdDate = createdDate;
		this.listQuestion = listQuestion;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}


	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}




	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	public List<Option> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<Option> listQuestion) {
		this.listQuestion = listQuestion;
	}



	public class Option
	{
		
		@SerializedName("id")
		private String id;
		
		@SerializedName("answer")
		private String answer = "";
		
		@SerializedName("answer_image")
		private String answerImage = "";
		
		@SerializedName("correct")
		private String correct = "";
		
		

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		
		
		public String getAnswerImage() {
			return answerImage;
		}
		public void setAnswerImage(String answerImage) {
			this.answerImage = answerImage;
		}
		
		
		public String getCorrect() {
			return correct;
		}
		public void setCorrect(String correct) {
			this.correct = correct;
		}
	}
	

}
