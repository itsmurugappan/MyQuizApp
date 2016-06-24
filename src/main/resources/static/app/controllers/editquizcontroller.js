(function() {
    
    var EditQuizController = function ($scope, $routeParams, quizFactory,$location) {
        var id = $routeParams.id;
		$scope.quiz = new Object();
        $scope.quiz.questions = new Array();
		$scope.options = ['Text', 'Drop Down'];
        
        function init() {
                    quizFactory.getQuiz(id)
                        .then(function(response) {
                    var quizObj = response.data;
                    $scope.quiz = quizObj.quiz;
                },  function(data, status, headers, config) {
                    //handle error
                });
        }        

        init();
        
            $scope.addQ = function () {
                var question = new Object();
                question.q = ' ';
                question.options = new Array();
                var o = new Object();
                question.options.push(o);
                $scope.quiz.questions.push(question);
			}
			
			$scope.addOption = function (question) {
                if(question.options === undefined)
                {
                    question.options = new Array();
                }

			     var o = new Object();
			     question.options.push(o);
  		};
		
		  $scope.saveQ = function ( path ) {
				quizFactory.updateQuiz($scope.quiz)
                .then(function(response) {
					$location.path( path );
                }, function(data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
				
  		};
        
            $scope.deleteQ = function ( id ) {
                if($scope.quiz.questions.length > 1) {
	                $scope.quiz.questions.splice(id, 1);
                }  
  		};
        
            $scope.deleteOption = function ( id, options) {
                if(options.length > 1) {
	                options.splice(id, 1);
                }  
  		};


  };
    


    
    EditQuizController.$inject = ['$scope', '$routeParams', 'quizFactory','$location'];

    angular.module('quizApp')
      .controller('EditQuizController', EditQuizController);
    
}());