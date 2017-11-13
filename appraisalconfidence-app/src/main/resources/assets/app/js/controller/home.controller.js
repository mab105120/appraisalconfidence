(function() {

    'use strict';

    home_controller.$inject = [
            '$scope',
            '$state',
            'authService',
            'appcon'
        ];


    function home_controller($scope, $state, authService, appcon) {
        init();
        function init() {
            $scope.$parent.startSpinner();
            $scope.showAlert = false;
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
        }

//        if(!authService.isAuthenticated()) {
//            alert('you must login to view this page!');
//            $state.go('welcome');
//        }

        $scope.start = function() {
            $state.go('questionnaire');
        };
    };

    module.exports = home_controller;
})();