(function() {

    'use strict';

    function navbar_directive() {
        return {
                templateUrl: 'app/template/navbar.html',
                controller: navbar_controller,
                controllerAs: 'vm'
        };
    }

    navbar_controller.$inject = ['$scope', 'authService'];

    function navbar_controller($scope, authService) {
        var vm = this; // why do this ?
        vm.auth = authService;

        $scope.login = function() {
            authService.login();
        }

        $scope.logout = function() {
            authService.logout();
        }
    };

    module.exports = navbar_directive;
})();