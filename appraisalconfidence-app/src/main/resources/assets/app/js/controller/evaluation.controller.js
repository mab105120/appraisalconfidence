(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$state',
            '$stateParams',
            '$http',
            '$window',
            'appcon',
            'authService',
            'toaster',
            '$sce'
        ];

    require('bootstrap-slider');

    function evaluation_controller($scope, $state, $stateParams, $http, $window, appcon, authService, toaster, $sce) {

        function init() {
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.currentEvaluation = $stateParams.id;

            // these vars are set by the eval directive when users click on supervisor reviews.
            $scope.modalCode = '';
            $scope.modalBodyTrusted = $sce.trustAsHtml($scope.modalBody);

            $scope.$watch('modalBody', function(val) {
                $scope.modalBodyTrusted = $sce.trustAsHtml(val);
            });

            $scope.modalTitle = '';

            $('#absConfidenceSlider').slider();
            $('#absConfidenceSlider').on('slide', function(slideEvt) {
                $scope.absConfidence = slideEvt.value;
            });
            $scope.$watch('absConfidence', function(value) {
                $('#absConfidenceSliderVal').text(value);
            });

            $('#relConfidenceSlider').slider();
            $('#relConfidenceSlider').on('slide', function(slideEvt) {
                $scope.relConfidence = slideEvt.value;
            });
            $scope.$watch('relConfidence', function(value) {
                $('#relConfidenceSliderVal').text(value);
            });

            // comment control
            $scope.comment = '';
            $scope.$watch('comment', function() {
                $scope.calculateRemainingChars();
            });
            $scope.remainingChars = 200;
            $scope.calculateRemainingChars = function() {
                $scope.remainingChars = 200 - $scope.comment.length;
            }

            // evaluation activity
            $scope.activities = [];
            $scope.time_modal_open;
            $('#reviewModal').on('hidden.bs.modal', function() {
                var closeTime = new Date().toISOString();
                $scope.activities.push({
                    evaluationCode: $stateParams.id,
                    selectedReview: $scope.modalCode,
                    openTime: $scope.time_modal_open,
                    closeTime: closeTime
                });
            });

            appcon.stepIsCompleted('EVALUATION_' + $stateParams.id)
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserEvaluation($stateParams.id)
                    .then(function success(response) {

                        // pre-populate fields
                        $scope.selectedTeacher = response.data.recommendationPick;
                        $('#teacher'+response.data.recommendationPick+'RadioBtn').prop('checked', true);
                        $scope.comment = response.data.comment;
                        $scope.absConfidence = response.data.absConfidence;
                        $('#absConfidenceSlider').slider().slider('setValue', response.data.absConfidence);
                        $scope.relConfidence = response.data.relConfidence;
                        $('#relConfidenceSlider').slider().slider('setValue', response.data.relConfidence);
                        $('#continueBtn').prop('disabled', false);

                        $scope.getTeacherPerformanceReviews();
                    }, function failure(response) {
                        var error = response.data === null ? 'Server unreachable' : response.data.message;
                        toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                        $scope.$parent.stopSpinner();
                    });
                }
                else {
                    $scope.getTeacherPerformanceReviews();
                }
            }, function failure(response) {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            });
        }

        init();

        $scope.getTeacherPerformanceReviews = function() {
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
        }

        $scope.saveAndContinue = function() {
            var TOTAL_EVALUATIONS = 15;
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

                if(nextEvaluationCode > 15)
                    $state.go('progress');
                else
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

    };

    module.exports = evaluation_controller;

})();