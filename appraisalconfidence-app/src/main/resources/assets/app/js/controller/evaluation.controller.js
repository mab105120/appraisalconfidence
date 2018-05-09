(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$state',
            '$stateParams',
            '$window',
            'appcon',
            'authService',
            'toaster',
            '$sce',
            'profileService',
            '$interval'
        ];

    require('bootstrap-slider');
    require('angular-timer');
    var _ = require('lodash');

    function evaluation_controller($scope, $state, $stateParams, $window, appcon, authService, toaster, $sce, profileService, $interval) {
        $scope.$parent.startSpinner();

        profileService.getProfile().then(
            function(response) {
                $scope.profile = response.data;
                init();
            }, handleFailure
        );

        function init() {
            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            initializeCurrentAndTotalEvaluationVars();

            $scope.isPractice = false;
            if($stateParams.id.toLowerCase().startsWith('p'))
                $scope.isPractice = true;

            $scope.isRelative = $scope.profile.relative;
            $scope.isExpert = $scope.profile.mode === 'EXPERT';

            paintPage();

            appcon.stepIsCompleted('EVALUATION_' + $stateParams.id)
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserEvaluation($stateParams.id, $scope.isRelative ? 'relative' : 'absolute')
                    .then(function success(response) {

                        $scope.isFamiliar = true;

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
                            if(!$scope.isExpert && !$scope.isPractice)
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
                    }, handleFailure);
                }
                else {
                    $scope.countdown = 0;
                    $scope.showTimer=true;
                    if(!$scope.isExpert)
                        $interval(function() {
                            $scope.countdown++;
                            if($scope.countdown >= 120) $scope.countdown += '+';
                        }, 1000, 120);
                    $scope.getTeacherPerformanceReviews();
                }
            }, function failure(response) {
                handleFailure(response);
            });


            function paintPage() {
                $scope.teacherOneLabel = $scope.isRelative ? 'Teacher 1' : 'Teacher';
                $scope.setClass = function() {
                   if($scope.isRelative)
                       return ['col-xs-5', 'col-xs-offset-2', 'text-center'];
                   else return ['col-xs-10', 'col-xs-offset-2', 'text-center'];
                }


                setUpProgressBar();

                setUpSliders();

                $scope.time_in = new Date().toISOString();

                // these vars are set by the eval directive when users click on supervisor reviews.
                $scope.modalCode = '';
                $scope.modalBodyTrusted = $sce.trustAsHtml($scope.modalBody);

                $scope.$watch('modalBody', function(val) {
                    $scope.modalBodyTrusted = $sce.trustAsHtml(val);
                });

                $scope.modalTitle = '';
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
            }

            function setUpSliders() {
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

            function setUpProgressBar() {
                var progressPercentage = ( $scope.currentEvaluation / $scope.totalEvaluations ) * 100;
                $scope.progressBarStyle = {
                    'width': progressPercentage + '%'
                };
                if ($scope.isPractice)
                    $scope.progressBarStyle['background-color'] = 'green';
            }

            function initializeCurrentAndTotalEvaluationVars() {
                if($stateParams.id.toLowerCase().startsWith('p')) {
                    $scope.currentEvaluation = $stateParams.id.substr(1);
                    $scope.totalEvaluations = $scope.profile.practice;
                    $scope.teacherEvaluationLabel = '(PRACTICE) Teacher Evaluation';
                    $('#evaluationProgressLabel').css('color', 'green');
                } else {
                    $scope.currentEvaluation = $stateParams.id;
                    $scope.totalEvaluations = $scope.profile.evaluations;
                    $scope.teacherEvaluationLabel = 'Teacher Evaluation';
                }
            }
        }

        $scope.getTeacherPerformanceReviews = function() {
            appcon.getReviews($stateParams.id, $scope.profile.mode).then(
                function(response) {
                    console.log('GET /api/performance-review/ ' + response.status);
                    $scope.evaluations = response.data;
                    $scope.$parent.stopSpinner();
                }, handleFailure);
        }

        $scope.saveAndContinue = function() {
            $scope.$parent.startSpinner();
            var checkQuality = false;
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You have to be logged in to perform this operation');
                $scope.$parent.stopSpinner();
                return;
            }

            // ensure all form required fields are filled (sliders don't work well with angular form validation)
            if(!formIsValid()) {
                $scope.$parent.stopSpinner();
                return;
            }

            // make sure participants are not just providing random answers
            if(checkQuality && !$scope.isExpert && !$scope.isFamiliar && !passQualityCheck()) {
                $scope.$parent.stopSpinner();
                return;
            }

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
                datetimeOut: $scope.time_out,
                mode: $scope.profile.mode
            }

            appcon.getExpertEvaluation(userEval).then( // TODO: This shouldn't be called for non-practice rounds
                function success(response) {
                    $scope.expert = response.data;
                    $scope.feedbackIsAvailable = feedbackIsAvailable();
                    if(responseChanged($scope.oldRes, userEval)) {
                        appcon.postUserEvaluation(payload, $scope.isRelative ? 'relative' : 'absolute')
                        .then(function success(response) {
                            toaster.pop('success', 'Saved!', 'Your response has been saved successfully!');
                        }, handleFailure);
                    }
                    showFeedback();
                    $scope.$parent.stopSpinner();
                },
                handleFailure);

            function showFeedback() {
                if($scope.isPractice)
                    $('#feedbackModal').modal('show');
                else
                    $scope.routeToNextPage();
            }

            function feedbackIsAvailable() {
                if($scope.profile.feedback === 'high')
                    return true;
                if($scope.profile.practice === 1)
                    return false;
                if($stateParams.id === 'P2')
                    return true;
                else return false;
            }

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
                if(!$scope.isPractice && !$scope.isExpert)
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
                    if($scope.studentLearningRating === undefined ||
                        $scope.instructionalPracticeRating === undefined || $scope.professionalismRating === undefined
                         || $scope.overallRating === undefined) {

                        alert('All fields in the form below are required. Please make sure to fill all fields out');
                        return false;
                    }
                    if((!$scope.isExpert && !$scope.isPractice) && $scope.promotionDecision === undefined) {
                        alert('All fields in the form below are required. Please make sure to fill all fields out');
                        return false;
                    }
                }
                return true;
            };

            function passQualityCheck() {
                if($scope.countdown != undefined && $scope.countdown < 120) {
                    alert("You need to spend at least two minutes on this evaluation before submitting. Please use this time to read teacher reviews below.");
                    return false;
                }
                if(!participantReadRequiredReviews(payload.activities)) {
                    alert('Please read AT LEAST 2 reviews PER TEACHER before submitting recommendation!\n\n' +
                        'WARNING: This application detects patterns of random responses. Participants WILL NOT be compensated for random responses.');
                    return false;
                }
                else return true;
            }

            function responseChanged(oldRes, newRes) {
                        if(oldRes === undefined)
                            return true;
                        else return ($scope.isRelative ? oldRes.recommendationPick != newRes.recommendationPick : false) ||
                                    oldRes.absConfidence != newRes.absConfidence ||
                                    oldRes.relConfidence != newRes.relConfidence ||
                                    ($scope.isRelative ? false : oldRes.promote != newRes.promote) ||
                                    ($scope.isRelative ? false : oldRes.studentLearning != newRes.studentLearning) ||
                                    ($scope.isRelative ? false : oldRes.instructionalPractice != newRes.instructionalPractice) ||
                                    ($scope.isRelative ? false : oldRes.professionalism != newRes.professionalism) ||
                                    ($scope.isRelative ? false : oldRes.overall != newRes.overall);
                    }
        }

        $scope.routeToNextPage = function() {
            $window.scrollTo(0, 0); // scroll to top
            $('#feedbackModal').modal('hide');
            $('body').removeClass('modal-open');
            $(".modal-backdrop").remove();
            var currentEval = parseInt($scope.currentEvaluation);
            if($scope.isPractice) {
                if(currentEval < $scope.profile.practice)
                    $state.go('evaluation', {id: 'P' + (currentEval + 1)});
                else
                    $state.go('evaluation', {id: 1});
            } else {
                var nextEvaluationCode = parseInt($stateParams.id) + 1;
                if(nextEvaluationCode > $scope.totalEvaluations) // reached the end of the experiment
                    $state.go('progress');
                else
                    $state.go('evaluation', {id: nextEvaluationCode});
            }
        };

        function handleFailure(response) {
            console.log(response);
            var error = response.data === null ? 'Server unreachable' : response.data.message;
            toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
            $scope.$parent.stopSpinner();
        }

    };

    module.exports = evaluation_controller;

})();