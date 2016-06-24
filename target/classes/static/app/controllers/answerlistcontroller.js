(function () {
    
    var AnswerListController = function ($scope, $log, quizFactory,$location,$routeParams) {
        var name = $routeParams.name;
        $scope.quizName = name;
        $scope.sortBy = 'name';
        $scope.reverse = false;
        $scope.answers = [];
        
        function init() {
               quizFactory.retrieveAnswerList(name)
                .then(function(response) {
                    $scope.answers = response.data;
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        }
        
        init();
        
        $scope.doSort = function(propName) {
           $scope.sortBy = propName;
           $scope.reverse = !$scope.reverse;
        };
		
	
		$scope.delete = function ( id ) {
				quizFactory.deleteAnswer(id)
                .then(function(response) {
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
                quizFactory.retrieveAnswerList(name)
                .then(function(response) {
                    $scope.answers = response.data;
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
  		};

 };
    
    
    AnswerListController.$inject = ['$scope', '$log', 'quizFactory','$location','$routeParams'];

    angular.module('quizApp')
      .controller('AnswerListController', AnswerListController);
    
}());