(function() {
    
    var app = angular.module('quizApp', ['ngRoute','ngFileUpload','directives',]);
    
    app.config(function($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'QuizListController',
                templateUrl: 'app/views/quizlist.html'
			})
            .when('/quizs/:id', {
                controller: 'ViewQuizController',
                templateUrl: 'app/views/viewquiz.html'
            })
            .when('/ty', {
                controller: 'TYController',
                templateUrl: 'app/views/thankyou.html'
				
            })
		    .when('/createQ', {
                controller: 'CreateQuizController',
                templateUrl: 'app/views/createquiz.html'
            })
            .when('/editQuiz/:id', {
                controller: 'EditQuizController',
                templateUrl: 'app/views/createquiz.html'
            })
            .when('/viewAnswers/:name', {
                controller: 'AnswerListController',
                templateUrl: 'app/views/answerlist.html'
            })
            .when('/viewAnswer/:id', {
                controller: 'ViewAnswersController',
                templateUrl: 'app/views/viewanswers.html'

            })
            .otherwise( { redirectTo: '/' } );
    });
	
}());