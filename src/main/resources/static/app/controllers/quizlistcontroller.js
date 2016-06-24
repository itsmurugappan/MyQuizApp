(function () {
    
    var QuizListController = function ($rootScope, $scope, $log, quizFactory, $location) {
        $scope.sortBy = 'name';
        $scope.reverse = false;
        $scope.quizs = [];
        
        function init() {
               quizFactory.getQuizList()
                .then(function(response) {
				   	$rootScope.authenticated = true;
                    $scope.quizs = response.data;
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        }
        
        init();
        
        $scope.doSort = function(propName) {
           $scope.sortBy = propName;
           $scope.reverse = !$scope.reverse;
        };
		
		$scope.createQ = function ( path ) {
				$location.path( path );
  		};
		
		$scope.delete = function ( id ) {
				quizFactory.deleteQuiz(id)
                .then(function(response) {
                    $scope.quizs = response.data;
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
  		};

 };
    
    
    QuizListController.$inject = ['$rootScope', '$scope', '$log', 'quizFactory','$location'];

    angular.module('quizApp')
      .controller('QuizListController', QuizListController);
    
}());