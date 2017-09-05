(function() {

    'use strict';

    ques_experience_controller.$inject = [
        '$scope',
        '$state'
    ];

    function ques_experience_controller($scope, $state) {
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
            $state.go('confidence');
        };
    }

    module.exports = ques_experience_controller;
})();