(function() {

    'use strict';

    questionnaire_controller.$inject = [
            '$scope',
            '$state',
            'appcon',
            'authService'
        ];

    function questionnaire_controller($scope, $state, appcon, authService) {
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
            if(!authService.isAuthenticated) {
                alert('You must be logged in to perform this operation!');
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
                $state.go('experience');
            }, function failure(response) {
                alert("Sorry we were unable to save your response. Reason (" + response.status + "): " + response.data.message);
                $scope.$parent.stopSpinner();
            });
        }
    }

    module.exports = questionnaire_controller;
})();