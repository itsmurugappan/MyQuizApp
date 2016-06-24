(function() {
    var quizFactory = function($http) {
    
        var factory = {};
        
        factory.getQuizList = function() {
            return $http.post('/quizList');
        };
        
        factory.getQuiz = function(id) {
            return $http({
            url: '/retrieveQuiz', 
            method: 'POST',
            data: {quizId: id}
            });
        };

        factory.updateQuizAnswers = function(quiz) {
            return $http({
            url: '/updateQuizAnswers', 
            method: 'POST',
            data: {q: quiz}
            });
        };
        factory.submitQuizAnswers = function(quiz) {
            return $http({
            url: '/saveQuizAnswers', 
            method: 'POST',
            data: {q: quiz}
            });
        };

		
		factory.submitQuiz = function(quiz) {
            return $http({
            url: '/saveQuiz', 
            method: 'POST',
            data: {quiz: quiz}
            });
        };
		
		factory.updateQuiz = function(quiz) {
            return $http({
            url: '/editQuiz', 
            method: 'POST',
            data: {quiz: quiz}
            });
        };

		
		factory.deleteQuiz = function(id) {
            return $http({
            url: '/deleteQuiz', 
            method: 'POST',
            data: {quizId: id}
            });
        };
        
		factory.retrieveAnswer = function(id) {
            return $http({
            url: '/retrieveAnswer', 
            method: 'POST',
            data: {id: id}
            });
        };

		factory.retrieveAnswerList = function(name) { 
            return $http({
            url: '/retrieveAnswerList', 
            method: 'POST',
            data: {name: name}
            });
        };

        factory.deleteAnswer = function(id) {
            return $http({
            url: '/deleteAnswer', 
            method: 'POST',
            data: {id: id}
            });
        };
		
        return factory;
    };
    
    quizFactory.$inject = ['$http'];
        
    angular.module('quizApp').factory('quizFactory', quizFactory);
                                           
}());