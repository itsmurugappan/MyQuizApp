(function() {
    
    var CreateQuizController = function ($scope, $routeParams, quizFactory,$location) {
		$scope.quiz = new Object();
        $scope.quiz.questions = new Array();
		$scope.options = ['Text', 'Drop Down'];
		$scope.sizeLimit      = 10585760; // 10MB in Bytes
  		$scope.uploadProgress = 0;
		$scope.creds = {
						  bucket: 'muruquiz',
						  access_key: 'AKIAJWFCR72O33SF2SEA',
						  secret_key: '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'
						}
        
        function init() {
                    var question = new Object();
                    question.q = ' ';
                    question.options = new Array();
                    var o = new Object();
                    question.options.push(o);
                    $scope.quiz.questions.push(question);
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
			
			$scope.addOption = function (q) {
                if(q.options === undefined)
                {
                    q.options = new Array();
                }

			     var o = new Object();
			     q.options.push(o);
  		};
		
		  $scope.saveQ = function ( path ) {
				quizFactory.submitQuiz($scope.quiz)
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
        
            $scope.deleteOption = function ( id,options ) {
                if(options.length > 1) {
	                options.splice(id, 1);
                }  
  		};
		
	$scope.upload = function(file,q) {
    AWS.config.update({ accessKeyId: $scope.creds.access_key, secretAccessKey: $scope.creds.secret_key });
    AWS.config.region = 'us-east-1';
    var bucket = new AWS.S3({ params: { Bucket: $scope.creds.bucket } });
    
    if(file) {
        // Perform File Size Check First
        var fileSize = Math.round(parseInt(file.size));
        if (fileSize > $scope.sizeLimit) {
          toastr.error('Sorry, your attachment is too big. <br/> Maximum '  + fileSizeLabel() + ' file attachment allowed','File Too Large');
          return false;
        }
        // Prepend Unique String To Prevent Overwrites
        var uniqueFileName = file.name;

        var params = { Key: uniqueFileName, ContentType: file.type, Body: file, ServerSideEncryption: 'AES256' };

        bucket.putObject(params, function(err, data) {
          if(err) {
            toastr.error(err.message,err.code);
            return false;
          }
          else {
            // Upload Successfully Finished
            toastr.success('File Uploaded Successfully', 'Done');
			q.imageLink = 'https://s3.amazonaws.com/' + $scope.creds.bucket + '/' + file.name;
            // Reset The Progress Bar
            setTimeout(function() {
              $scope.uploadProgress = 0;
              $scope.$digest();
            }, 4000);
          }
        })
        .on('httpUploadProgress',function(progress) {
          $scope.uploadProgress = Math.round(progress.loaded / progress.total * 100);
          $scope.$digest();
        });
      }
      else {
        // No File Selected
        toastr.error('Please select a file to upload');
      }
    };

    fileSizeLabel = function() {
    // Convert Bytes To MB
    return Math.round($scope.sizeLimit / 1024 / 1024) + 'MB';
  };

		
  };
    


    
    CreateQuizController.$inject = ['$scope', '$routeParams', 'quizFactory','$location'];

    angular.module('quizApp')
      .controller('CreateQuizController', CreateQuizController);
    
}());