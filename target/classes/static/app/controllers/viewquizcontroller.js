(function() {
    
    var ViewQuizController = function ($scope, $routeParams, quizFactory,$location) {
        var id = $routeParams.id;
        $scope.quizObj = null;
        
        function init() {

             quizFactory.getQuiz(id)
                .then(function(response) {
                    $scope.quizObj = response.data;
                }, function(data, status, headers, config) {
                    //handle error
                });

        }        

        init();
        
            $scope.ty = function ( path ) {
				quizFactory.submitQuizAnswers($scope.quizObj.quiz)
                .then(function(response) {
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
				$location.path( path );
  };
    };
    


    
    ViewQuizController.$inject = ['$scope', '$routeParams', 'quizFactory','$location'];

    angular.module('quizApp')
      .controller('ViewQuizController', ViewQuizController);
    
}());