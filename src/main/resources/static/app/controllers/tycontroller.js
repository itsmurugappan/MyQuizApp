(function () {
    
    var TYController = function ($rootScope,$scope, $log, quizFactory) {

         $scope.slides = [];
$scope.myInterval = 5000;
        
        function init() {
               quizFactory.getQuizList()
                .then(function(response) {
				   	$rootScope.authenticated = true;
                    $scope.slides = response.data;
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        }
        
        init();
    };
    TYController.$inject = ['$rootScope','$scope', '$log', 'quizFactory'];

    angular.module('quizApp')
      .controller('TYController', TYController);
    
}());