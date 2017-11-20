(function() {

    'use strict';

    questionnaire_controller.$inject = [
            '$scope',
            '$state',
            'appcon',
            'authService',
            'toaster'
        ];

    function questionnaire_controller($scope, $state, appcon, authService, toaster) {

        function init() {
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            appcon.stepIsCompleted('QUEST_DEMO')
            .then(function success(response) {
                console.log(response);
                if(response.data === true) {
                    appcon.getUserDemographics()
                    .then(function success(response) {
                        var userDemographics = response.data;
                        // populate the fields
                        $scope.age = parseInt(userDemographics.age);
                        $scope.gender = userDemographics.gender;
                        $scope.education = userDemographics.education;
                        $scope.division = userDemographics.division;

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

        $scope.ageGroup = [
            '[20-30]',
            '[30-40]',
            '[40-50]',
            '[50-60]',
            '60+'
        ];

        $scope.genderGroup = ['Male', 'Female'];

        $scope.eductionGroup = [
            'No degree',
            'Associate degree',
            'Bachelor degree',
            'Master degree',
            'Doctorate degree'
        ];

        $scope.divisionGroup = [
            'Technology',
            'Finance',
            'Operations',
            'Real Estate',
            'Investment Banking',
            'Securities',
            'Other'
        ];

        $scope.submit = function() {
            if(!authService.isAuthenticated()) {
                toaster.pop('error','Authentication Issue','You must be logged in to perform this action');
                return;
            }
            var user = {
                age: $scope.age,
                gender: $scope.gender,
                education: $scope.education,
                division: $scope.division
            };
            $scope.$parent.startSpinner();
            appcon.postUserDemographic(user)
            .then(function success(response) {
                $scope.$parent.stopSpinner();
                toaster.pop('success','Saved!','Your response has been saved successfully!');
                $state.go('experience');
            }, function failure(response) {
                var errorMessage = (response.data === null) ? 'Server unreachable' : response.data.message;
                toaster.pop('error','Error', "Sorry we were unable to save your response. Reason (" + response.status + "): " + errorMessage);
                $scope.$parent.stopSpinner();
            });
        }
    }

    module.exports = questionnaire_controller;
})();