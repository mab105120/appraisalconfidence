(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$state',
            '$stateParams',
            '$window',
            'appcon',
            'authService',
            'toaster',
            '$sce'
        ];

    require('bootstrap-slider');

    function evaluation_controller($scope, $state, $stateParams, $window, appcon, authService, toaster, $sce) {

        function init() {
            $scope.time_in = new Date().toISOString();
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.currentEvaluation = $stateParams.id;
            $scope.totalEvaluations = localStorage.getItem('totalEvaluations');
            var progressPercentage = ( $scope.currentEvaluation / $scope.totalEvaluations ) * 100;
            $scope.progressBarStyle = {
                'width': progressPercentage + '%'
            }

            // these vars are set by the eval directive when users click on supervisor reviews.
            $scope.modalCode = '';
            $scope.modalBodyTrusted = $sce.trustAsHtml($scope.modalBody);

            $scope.$watch('modalBody', function(val) {
                $scope.modalBodyTrusted = $sce.trustAsHtml(val);
            });

            $scope.modalTitle = '';

            $('#absConfidenceSlider').slider();
            $('#absConfidenceSlider').on('change', function(slideEvt) {
                $scope.absConfidence = slideEvt.value.newValue;
            });

            $('#relConfidenceSlider').slider();
            $('#relConfidenceSlider').on('change', function(slideEvt) {
                $scope.relConfidence = slideEvt.value.newValue;
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
                        $scope.oldRes = response.data;

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
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You have to be logged in to perform this operation');
                return;
            }
            if($scope.selectedTeacher === undefined || $scope.relConfidence === undefined || $scope.absConfidence === undefined) {
                alert('All fields in the form below are required. Please make sure to fill all fields out');
                return;
            }
            $scope.time_out = new Date().toISOString();
            var userEval = {
                evaluationCode: $stateParams.id,
                recommendationPick: $scope.selectedTeacher,
                relConfidence: $scope.relConfidence,
                absConfidence: $scope.absConfidence,
                comment: $scope.comment
            };
            var payload = {
                recommendation: userEval,
                activities: $scope.activities,
                datetimeIn: $scope.time_in,
                datetimeOut: $scope.time_out
            }
            var nextEvaluationCode = parseInt($stateParams.id) + 1;
            if(responseChanged($scope.oldRes, userEval)) {
                $scope.$parent.startSpinner();
                appcon.postUserEvaluation(payload)
                .then(function success(response) {
                    toaster.pop('success', 'Saved!', 'Your response has been saved successfully!');
                    routeToNextPage();
                    $scope.$parent.stopSpinner();
                }, function failure(response) {
                    var error = response.data === null ? 'Server unreachable' : response.data.message;
                    toaster.pop('error', 'Error', 'Oops! we were not able to save your response: ' + error);
                    console.log('Error Object');
                    console.log(response);
                    $scope.$parent.stopSpinner();
                });
            } else {
                routeToNextPage();
            }
            function routeToNextPage() {
                $window.scrollTo(0, 0); // scroll to top
                var nextEvaluationCode = parseInt($stateParams.id) + 1;
                if(nextEvaluationCode > $scope.totalEvaluations) // reached the end of the experiment
                    $state.go('progress');
                else
                    $state.go('evaluation', {id: nextEvaluationCode});
            }
        }

        function responseChanged(oldRes, newRes) {
            if(oldRes === undefined)
                return true;
            else return oldRes.recommendationPick != newRes.recommendationPick ||
                        oldRes.absConfidence != newRes.absConfidence ||
                        oldRes.relConfidence != newRes.relConfidence ||
                        oldRes.comment !== newRes.comment;
        }

    };

    module.exports = evaluation_controller;

})();