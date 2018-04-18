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
    var _ = require('lodash');

    function evaluation_controller($scope, $state, $stateParams, $window, appcon, authService, toaster, $sce) {

        function init() {

            $scope.isRelative = false;
            $scope.teacherOneLabel = $scope.isRelative ? 'Teacher 1' : 'Teacher';

            $scope.setClass = function() {
                if($scope.isRelative)
                    return ['col-xs-5', 'col-xs-offset-2', 'text-center'];
                else return ['col-xs-10', 'col-xs-offset-2', 'text-center'];
            }

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

            setupSliders();

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

                        if($scope.isRelative) {
                            $scope.selectedTeacher = response.data.recommendationPick;
                            $('#teacher'+response.data.recommendationPick+'RadioBtn').prop('checked', true);
                            $scope.comment = response.data.comment;
                        } else {
                            $scope.studentLearningRating = response.data.studentLearning;
                            $('#studentLearningSlider').slider().slider('setValue', response.data.studentLearning);
                            $scope.instructionalPracticeRating = response.data.instructionalPractice;
                            $('#instructionalPracticeSlider').slider().slider('setValue', response.data.instructionalPractice);
                            $scope.professionalismRating = response.data.professionalism;
                            $('#professionalismSlider').slider().slider('setValue', response.data.professionalism);
                            $scope.overallRating = response.data.overall;
                            $('#overallSlider').slider().slider('setValue', response.data.overall);
                            $scope.promotionDecision = response.data.promote;
                            if(response.data.promote.toUpperCase() === 'Y')
                                $('#promoteYes').prop('checked', true);
                            else $('#promoteNo').prop('checked', true);
                        }

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

            function setupSliders() {
                $('#absConfidenceSlider').slider();
                $('#absConfidenceSlider').on('change', function(slideEvt) {
                    $scope.absConfidence = slideEvt.value.newValue;
                });

                $('#relConfidenceSlider').slider();
                $('#relConfidenceSlider').on('change', function(slideEvt) {
                    $scope.relConfidence = slideEvt.value.newValue;
                });

                $('#studentLearningSlider').slider();
                $('#studentLearningSlider').on('change', function(slideEvt) {
                    $scope.studentLearningRating = slideEvt.value.newValue;
                });

                $('#instructionalPracticeSlider').slider();
                $('#instructionalPracticeSlider').on('change', function(slideEvt) {
                    $scope.instructionalPracticeRating = slideEvt.value.newValue;
                });

                $('#professionalismSlider').slider();
                $('#professionalismSlider').on('change', function(slideEvt) {
                    $scope.professionalismRating = slideEvt.value.newValue;
                });

                $('#overallSlider').slider();
                $('#overallSlider').on('change', function(slideEvt) {
                    $scope.overallRating = slideEvt.value.newValue;
                });
            }
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
            // ensure all form required fields are filled (sliders don't work well with angular form validation)
            if(!formIsValid()) return;

            $scope.time_out = new Date().toISOString();
            var userEval = {
                evaluationCode: $stateParams.id,
                recommendationPick: $scope.selectedTeacher,
                relConfidence: $scope.relConfidence,
                absConfidence: $scope.absConfidence,
                studentLearning: $scope.studentLearningRating,
                instructionalPractice: $scope.instructionalPracticeRating,
                professionalism: $scope.professionalismRating,
                overall: $scope.overallRating,
                promote: $scope.promotionDecision,
                comment: $scope.comment
            };
            var payload = {
                recommendation: userEval,
                activities: $scope.activities,
                datetimeIn: $scope.time_in,
                datetimeOut: $scope.time_out
            }

            // make sure participants are not just providing random answers
            if(!passQualityCheck()) return;

            var nextEvaluationCode = parseInt($stateParams.id) + 1;
            if(responseChanged($scope.oldRes, userEval)) {
                $scope.$parent.startSpinner();
                appcon.postUserEvaluation(payload, $scope.isRelative ? 'relative' : 'absolute')
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
            };

            function participantReadRequiredReviews(activities) {
                selectedReviews = [];
                angular.forEach(activities, function(activity) {
                    selectedReviews.push(activity.selectedReview);
                });
                var uniqueCodes = _.uniq(selectedReviews);
                console.log(uniqueCodes);
                var t1 = 0, t2 = 0;
                angular.forEach(uniqueCodes, function(code) {
                    var t = code.substr(code.indexOf('-') + 1, 2); // return the teacher from the code
                    if (t === 'T1')
                        t1++;
                    else if (t === 'T2')
                        t2++;
                });
                if($scope.isRelative) {
                    if(t1 < 2 || t2 < 2) return false;
                } else {
                    if(t1 < 2) return false;
                }
                return true;
            };

            function formIsValid() {
                if($scope.relConfidence === undefined || $scope.absConfidence === undefined) {
                    alert('All fields in the form below are required. Please make sure to fill all fields out');
                    return false;
                }
                if($scope.isRelative) {
                    if($scope.selectedTeacher === undefined) {
                        alert('All fields in the form below are required. Please make sure to fill all fields out');
                        return false;
                    }
                } else {
                    if($scope.promotionDecision === undefined || $scope.studentLearningRating === undefined ||
                        $scope.instructionalPracticeRating === undefined || $scope.professionalismRating === undefined
                         || $scope.overallRating === undefined) {

                        alert('All fields in the form below are required. Please make sure to fill all fields out');
                        return false;
                    }
                }
                return true;
            };

            function passQualityCheck() {
                if(!participantReadRequiredReviews(payload.activities)) {
                    alert('Please read AT LEAST 2 reviews PER TEACHER before submitting recommendation!\n\n' +
                        'WARNING: This application detects patterns of random responses. Participants WILL NOT be compensated for random responses.');
                    return false;
                }
                else return true;
            }
        }

        function responseChanged(oldRes, newRes) {
            if(oldRes === undefined)
                return true;
            else return $scope.isRelative ? oldRes.recommendationPick != newRes.recommendationPick : true ||
                        oldRes.absConfidence != newRes.absConfidence ||
                        oldRes.relConfidence != newRes.relConfidence ||
                        $scope.isRelative ? true : oldRes.promotionDecision != newRes.promotionDecision ||
                        $scope.isRelative ? true :oldRes.studentLearningRating != newRes.studentLearningRating ||
                        $scope.isRelative ? true :oldRes.instructionalPracticeRating != newRes.instructionalPracticeRating ||
                        $scope.isRelative ? true :oldRes.professionalismRating != newRes.professionalismRating ||
                        $scope.isRelative ? true :oldRes.overallRating != oldRes.overallRating ||
                        oldRes.comment !== newRes.comment;
        }

    };

    module.exports = evaluation_controller;

})();