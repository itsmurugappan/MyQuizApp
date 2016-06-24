(function () {
    
    var TYController = function ($scope, $log, quizFactory) {
        
        function init() {
        }
        
        init();
        
    };
    TYController.$inject = ['$scope', '$log', 'quizFactory'];

    angular.module('quizApp')
      .controller('TYController', TYController);
    
}());