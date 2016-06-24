(function() {
    
    var ViewAnswersController = function ($scope, $routeParams, quizFactory,$location) {
        var id = $routeParams.id;
        $scope.a = null;
        
        function init() {

             quizFactory.retrieveAnswer(id)
                .then(function(response) {
                    $scope.a = response.data;
                }, function(data, status, headers, config) {
                    //handle error
                });

        }        

        init();
        
            $scope.saveAnswers = function ( path ) {
                var total = 0;
                for (i = 0; i < $scope.a.q.questions.length; i++) { 
                    var question = $scope.a.q.questions[i];
                    total += parseInt(question.marks);
                }
                $scope.a.q.sum = total;
				quizFactory.updateQuizAnswers($scope.a.q)
                .then(function(response) {
                    $location.path( path );
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
            };

}; 
    
    ViewAnswersController.$inject = ['$scope', '$routeParams', 'quizFactory','$location'];

    angular.module('quizApp')
      .controller('ViewAnswersController', ViewAnswersController);
    
}());