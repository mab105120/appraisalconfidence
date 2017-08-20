(function() {

    'use strict';

    function navbar_directive() {
        return {
                templateUrl: 'app/template/navbar.html',
                controller: navbar_controller,
                controllerAs: 'vm'
        };
    }

    navbar_controller.$inject = ['authService'];

    function navbar_controller(authService) {
        var vm = this; // why do this ?
        vm.auth = authService;
    };

    module.exports = navbar_directive;
})();