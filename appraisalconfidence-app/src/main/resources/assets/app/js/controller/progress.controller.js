(function() {

    progress_controller.$inject = [
        '$scope',
        '$state',
        'appcon',
        'authService'
    ];

    function progress_controller($scope, $state, appcon, authService) {

        function init() {
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.qual_redirect_url = localStorage.getItem('qual_redirect_url');

            $scope.showSubmit = false;

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
                var totalEvaluations = localStorage.getItem('totalEvaluations');
                for(i = 1; i <= totalEvaluations; i++) {
                    $scope.rows.push(
                        {
                            id: 'EVALUATION_' + i,
                            display: 'Teacher Evaluation ' + i + ' / ' + totalEvaluations
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

                var allCompleted = true;

                angular.forEach($scope.rows, function(item) {
                    if(completed.get(item.id) !== undefined)
                        item.status = 'Complete';
                    else if (item.id === next) {
                        item.status = 'Next';
                        allCompleted = false;
                    }
                    else {
                        item.status = 'Not Started';
                        allCompleted = false;
                    }
                });
                $scope.showSubmit = allCompleted;
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
                $state.go('evaluation', {id: parseInt(id.substr(id.indexOf('_') + 1))});
            }
        }
    }

    module.exports = progress_controller;
})();