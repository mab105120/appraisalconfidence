(function() {

    'use strict';

    function navbar_directive() {
        return {
                templateUrl: 'app/template/navbar.html',
                controller: navbar_controller,
                controllerAs: 'vm'
        };
    }

    navbar_controller.$inject = ['$scope', 'authService', '$timeout'];

    function navbar_controller($scope, authService, $timeout) {
        var vm = this; // why do this ?
        vm.auth = authService;
        $scope.login = function() {
            authService.login();
        }

        $scope.logout = function() {
            $scope.startSpinner();
            authService.logout();
            $scope.stopSpinner();
        }
    };

    module.exports = navbar_directive;
})();