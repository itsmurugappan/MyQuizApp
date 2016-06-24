(function() {
 var LoginController = function($rootScope, $log, $location, $route, quizFactory) {
			
			var self = this;

			self.tab = function(route) {
				return $route.current && route === $route.current.controller;
			};

			function authenticate (credentials) {

				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				 quizFactory.doAuth(headers)
				 .then(function(response) {
					if (response.data.name) {
						$rootScope.authenticated = true;
					} else {
						$rootScope.authenticated = false;
					}
					}, function(data, status, headers, config) {
						$rootScope.authenticated = false;
						$log.log(data.error + ' ' + status);
					});
			}

			authenticate();

			self.credentials = {};
			self.login = function() {
					authenticate();
					if (authenticated) {
						console.log("Login succeeded")
						$location.path("/");
						self.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed")
						$location.path("/login");
						self.error = true;
						$rootScope.authenticated = false;
					}
			};	
		};
    
    
    LoginController.$inject = ['$rootScope', '$log', '$location','$route','quizFactory'];

    angular.module('quizApp')
      .controller('LoginController', LoginController);
    
}());