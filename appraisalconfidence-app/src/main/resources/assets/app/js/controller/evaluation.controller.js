(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$state',
            '$stateParams',
            '$http',
            '$window',
            'appcon',
            'authService',
            'toaster'
        ];

    require('bootstrap-slider');

    function evaluation_controller($scope, $state, $stateParams, $http, $window, appcon, authService, toaster) {
        var TOTAL_EVALUATIONS = 10;
        $scope.currentEvaluation = $stateParams.id;

        $('#slider1').slider();
        $('#slider1').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.absConfidence = val;
            $('#slider1Val').text(val);
        });

        $('#slider2').slider();
        $('#slider2').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.relConfidence = val;
            $('#slider2Val').text(val);
        });

        $scope.selectedEvaluation = '';
        $scope.selectedEvaluationTitle = '';

        appcon.getReviews($stateParams.id).then(
            function(response) {
                console.log('GET /api/performance-review/ ' + response.status);
                $scope.evaluations = response.data;
                $scope.$parent.stopSpinner();
            },
            function(response) {
                var errorMessage = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Unable to get teacher reviews: ' + errorMessage);
                console.log('GET /api/performance-review/ ' + response.status);
                console.log(response);
                $scope.$parent.stopSpinner();
            }
        );

        // Comment control
        var comment_max_length = 200;
        $scope.comment = '';
        $scope.remainingChars = 200;
        $scope.calculateRemainingChars = function() {
            $scope.remainingChars = comment_max_length - $scope.comment.length;
        }

        $scope.saveAndContinue = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You have to be logged in to perform this operation');
                return;
            }
            $scope.$parent.startSpinner();
            var userEval = {
                evaluationCode: $stateParams.id,
                recommendationPick: $scope.selectedTeacher,
                relConfidence: $scope.relConfidence,
                absConfidence: $scope.absConfidence,
                comment: $scope.comment
            };
            $scope.$parent.startSpinner();
            appcon.postUserEvaluation(userEval)
            .then(function success(response) {
                toaster.pop('success', 'Saved!', 'Your response has been saved successfully!');
                if($stateParams.id === TOTAL_EVALUATIONS)
                    $state.go('end');
                var nextEvaluationCode = parseInt($stateParams.id) + 1;
                $window.scrollTo(0, 0); // scroll to top
                $state.go('evaluation', {id: nextEvaluationCode});
                $scope.$parent.stopSpinner();
            }, function failure(response) {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we were not able to save your response: ' + error);
                console.log('Error Object');
                console.log(response);
                $scope.$parent.stopSpinner();
            });
        }

        $scope.back = function() {
            toaster.pop('warning', 'Operation unavailable', 'Back button is currently unavailable!');
        }

    };


    module.exports = evaluation_controller;

})();