(function() {

    progress_controller.$inject = [
        '$scope',
        '$state',
        'appcon'
    ];

    function progress_controller($scope, $state, appcon) {

        function init() {
            $scope.$parent.startSpinner();

            $scope.rows = [
                {
                    id: 'QUEST_DEMO',
                    display: 'General questionnaire'
                },
                {
                    id: 'QUEST_EXP',
                    display: 'Professional experience questionnaire'
                },
                {
                    id: 'QUEST_CON',
                    display: 'Judgment confidence questionnaire'
                }
            ];

            function addEvaluationsToRows() {
                for(i = 1; i <= 15; i++) {
                    $scope.rows.push(
                        {
                            id: 'EVALUATION_' + i,
                            display: 'Teacher Evaluation ' + i + ' / 15'
                        }
                    );
                }
            }

            addEvaluationsToRows();

            appcon.getProgress()
            .then(function success(response) {
                var completed = new Map();
                var next = response.data.next;
                angular.forEach(response.data.completed, function(item) {
                    completed.set(item, item);
                });

                angular.forEach($scope.rows, function(item) {
                    if(completed.get(item.id) !== undefined)
                        item.status = 'Complete';
                    else if (item.id === next)
                        item.status = 'Next';
                    else item.status = 'Not Started';
                });

                $scope.$parent.stopSpinner();
            }, function failure(response) {
                console.log(response);
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            });
        }

        init();

        $scope.edit = function(id) {
            if(id === 'QUEST_DEMO') {
                $state.go('questionnaire');
            } else if (id === 'QUEST_EXP') {
                $state.go('experience');
            } else if (id === 'QUEST_CON') {
                $state.go('confidence');
            } else if(id.startsWith('EVALUATION')) {
                $state.go('evaluation', {id: parseInt(id.substr(id.length - 1))});
            }
        }
    }

    module.exports = progress_controller;
})();