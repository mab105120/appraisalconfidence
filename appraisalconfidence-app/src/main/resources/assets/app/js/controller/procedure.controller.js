(function() {

    procedure_controller.$inject = ['$scope', '$state', 'authService', '$window'];

    function procedure_controller($scope, $state, authService, $window) {
//        if(!authService.isAuthenticated()) {
//            alert('You are not logged in. You need to log in to view this page.');
//            authService.login();
//        }

        console.log('the controller is also executing');
        $scope.checkboxStatus = false;
        // TODO replace this with angular form
        $scope.checkBox = function (status) {
            if(status === false) status = true;
            else status = false;
        }

        $scope.next = function() {
            $window.scrollTo(0, 0);
            $state.go('tenure');
        }
    }

    module.exports = procedure_controller;

})();