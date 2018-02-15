(function() {

    'use strict';

    home_controller.$inject = [
            '$scope',
            '$state',
            'authService',
            'appcon',
            'toaster'
        ];


    function home_controller($scope, $state, authService, appcon, toaster) {
        init();
        function init() {
            $scope.$parent.startSpinner();

            if(!authService.isAuthenticated()) {
                alert('You are not logged in. You need to log in to view this page.');
                authService.login();
            }

            $scope.showAlert = false;
            $scope.$parent.startSpinner();
            appcon.getExperimentSettings()
            .then(function success(response) {
                var data = response.data;
                console.log(data);
                $scope.$parent.totalEvaluations = data.totalEvaluations;
                $scope.$parent.duration = data.duration;
                $scope.duration = data.duration;
                appcon.getProgress()
                .then(function success(response) {
                    if(response.data.completed.length !== 0)
                        $scope.showAlert = true;
                        $scope.$parent.stopSpinner();
                }, function failure(response) {
                    console.log(response);
                    var error = response.data === null ? 'Server unreachable' : response.data.message;
                    toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                    $scope.$parent.stopSpinner();
                });
            }, function failure(response){
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we are having a bit of trouble! Details: ' + error);
                $scope.$parent.stopSpinner();
            });
        }
        $scope.start = function() {
            $state.go('procedure');
        };
    };

    module.exports = home_controller;
})();