(function() {

    'use strict';

    ques_experience_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'authService'
    ];

    function ques_experience_controller($scope, $state, appcon, authService) {
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
                alert('You have to be logged in to perform this operation!');
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
                user.totalCandidate = $scope.interviewees;
            }
            $scope.$parent.startSpinner();
            appcon.postUserExperience(user)
            .then(function success(response) {
                $scope.$parent.stopSpinner();
                $state.go('confidence');
            }, function failure(response) {
                $scope.$parent.stopSpinner();
                alert('Error saving data: ' + response.message);
            });
        };
    }

    module.exports = ques_experience_controller;
})();