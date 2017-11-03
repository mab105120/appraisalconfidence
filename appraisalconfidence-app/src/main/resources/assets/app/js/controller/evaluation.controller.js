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

        function init() {
            $scope.$parent.startSpinner();
            appcon.questionnaireCompleted('EVALUATION_' + $stateParams.id)
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserEvaluation($stateParams.id)
                    .then(function success(response) {

                        $scope.selectedTeacher = response.data.recommendationPick;
                        $scope.comment = response.data.comment;
                        $scope.absConfidence = response.data.absConfidence;
                        $('#slider1').slider().slider('setValue', response.data.absConfidence);
                        $scope.relConfidence = response.data.relConfidence;
                        $('#slider2').slider().slider('setValue', response.data.relConfidence);

                        $scope.$parent.stopSpinner();
                    }, function failure(response) {
                        var error = response.data === null ? 'Server unreachable' : response.data.message;
                        toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                        $scope.$parent.stopSpinner();
                    });
                }
                else
                    $scope.$parent.stopSpinner();
            }, function failure(response) {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            });
        }

         init();

        var TOTAL_EVALUATIONS = 15;
        $scope.currentEvaluation = $stateParams.id;

        $('#slider1').slider();
        $('#slider1').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.absConfidence = val;
            $('#slider1Val').text(val);
        });

        $scope.$watch('absConfidence', function(value) {
            $('#slider1Val').text(value);
        });

        $scope.$watch('relConfidence', function(value) {
            $('#slider2Val').text(value);
        });

        $('#slider2').slider();
        $('#slider2').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.relConfidence = val;
            $('#slider2Val').text(val);
        });

        $scope.selectedEvaluationCode = '';
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

        // evaluation activity
        $scope.activities = [];
        $scope.time_modal_open;
        $('#reviewModal').on('hidden.bs.modal', function() {
            var closeTime = new Date().toISOString();
            $scope.activities.push({
                evaluationCode: $stateParams.id,
                selectedReview: $scope.selectedEvaluationCode,
                openTime: $scope.time_modal_open,
                closeTime: closeTime
            });
        });

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
            var payload = {
                recommendation: userEval,
                activities: $scope.activities
            }
            $scope.$parent.startSpinner();
            appcon.postUserEvaluation(payload)
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