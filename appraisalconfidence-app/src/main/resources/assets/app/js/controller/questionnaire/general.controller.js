(function() {

    'use strict';

    questionnaire_controller.$inject = ['$scope', '$state'];

    function questionnaire_controller($scope, $state) {
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
            $state.go('experience');
        }
    }

    module.exports = questionnaire_controller;
})();