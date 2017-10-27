(function() {

    'use strict';

    ques_experience_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'toaster',
        'authService'
    ];

    function ques_experience_controller($scope, $state, appcon, toaster, authService) {

        function init() {
            $scope.$parent.startSpinner();
            appcon.questionnaireCompleted('QUEST_EXP')
            .then(function success(response) {
                if(response.data === true) {
                    appcon.getUserExperience()
                    .then(function success(response) {
                        var userExperience = response.data;
                        // populate form fields
                        $scope.title = userExperience.title;
                        $scope.subordinates = parseInt(userExperience.subordinates);
                        $scope.professionalExperience = parseInt(userExperience.professionalExperience);
                        $scope.paExperience = parseInt(userExperience.appraisalExperience);
                        $scope.reviewsUpToDate = parseInt(userExperience.totalReviews);
                        $scope.revieweesUpToDate = parseInt(userExperience.totalReviewees);
                        $scope.personnelSelection = userExperience.personnelSelection;
                        if(userExperience.personnelSelection === 'Yes')
                            $scope.interviewees = parseInt(userExperience.totalCandidates);

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

        $scope.titleGroup = [
            'Contingent Worker',
            'Analyst',
            'Associate',
            'Vice President',
            'Managing Director',
            'Partner',
            'Other'
        ];

        $scope.fiveScaleGroup = [
            '[0-5]',
            '[5-10]',
            '[10-15]',
            '[15-20]',
            '20+'
        ];

        $scope.twentyScaleGroup = [
            '[0-20]',
            '[20-40]',
            '[40-60]',
            '[60-80]',
            '80+',
        ];

        $scope.binaryGroup = [
            'Yes',
            'No'
        ];

        $scope.fiftyScaleGroup = [
            '[0-50]',
            '[50-100]',
            '[100-150]',
            '[150-200]',
            '[200-250]',
            '250+'
        ];

        $scope.$watch('personnelSelection', function() {
            if($scope.personnelSelection === 'Yes') $scope.personnelSelectionBool = true;
            else $scope.personnelSelectionBool = false;
        })

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error', 'Error', 'You have to be logged in to perform this operation');
                return;
            }
            var user = {
                title: $scope.title,
                subordinates: $scope.subordinates,
                professionalExperience: $scope.professionalExperience,
                appraisalExperience: $scope.paExperience,
                totalReviews: $scope.reviewsUpToDate,
                totalReviewees: $scope.revieweesUpToDate,
                personnelSelection: $scope.personnelSelection
            };
            if($scope.interviewees !== undefined) {
                user.totalCandidates = $scope.interviewees;
            }
            $scope.$parent.startSpinner();
            appcon.postUserExperience(user)
            .then(function success(response) {
                $scope.$parent.stopSpinner();
                toaster.pop('success', 'Saved!', 'Your response have been saved');
                $state.go('confidence');
            }, function failure(response) {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                $scope.$parent.stopSpinner();
                toaster.pop('error', 'Error', 'Oops! we were not able to save your response: ' + error);
            });
        };
    }

    module.exports = ques_experience_controller;
})();