(function() {

    procedure_controller.$inject = ['$scope', '$state', 'authService'];

    function procedure_controller($scope, $state, authService) {
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
            $state.go('evaluation', { id: 1 });
        }
    }

    module.exports = procedure_controller;

})();