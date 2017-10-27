(function() {

    progress_controller.$inject = [
        '$scope',
        '$state'
    ];

    function progress_controller($scope, $state) {
        $scope.rows = [
            {
                step: "General questionnaire",
                status: "Complete"
            },
            {
                step: "Professional experience questionnaire",
                status: "Complete"
            },
            {
                step: "Judgment confidence questionnaire",
                status: "Complete"
            },
            {
                step: "Teacher Evaluation 1 / 15",
                status: "Complete"
            },
            {
                step: "Teacher Evaluation 2 / 15",
                status: "Not Started"
            }

        ];

        $scope.edit = function(step) {
            if(step === 'General questionnaire') {
                $state.go('questionnaire');
            } else if (step === 'Professional experience questionnaire') {
                $state.go('experience');
            }
            console.log('This function will take you to step: ' + step);
        }
    }

    module.exports = progress_controller;
})();