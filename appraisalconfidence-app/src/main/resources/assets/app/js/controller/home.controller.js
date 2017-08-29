(function() {

    'use strict';

    home_controller.$inject = ['$scope', '$state', 'authService'];


    function home_controller($scope, $state, authService) {

        if(!authService.isAuthenticated()) {
            alert('you must login to view this page!');
            $state.go('welcome');
        }

        $scope.start = function() {
            $state.go('procedure');
        };
    };

    module.exports = home_controller;
})();