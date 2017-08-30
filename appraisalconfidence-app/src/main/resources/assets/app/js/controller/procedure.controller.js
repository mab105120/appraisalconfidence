(function() {

    procedure_controller.$inject = ['$scope', '$state', 'authService'];

    function procedure_controller($scope, $state, authService) {

        if(!authService.isAuthenticated()) {
            alert('you must login to view this page!');
            $state.go('welcome');
        }

        $scope.checkboxStatus = false;

        $scope.checkBox = function (status) {
            if(status === false) status = true;
            else status = false;
        }

        $scope.next = function() {
            $state.go('evaluation', { id: 1 });
        }
    };

    module.exports = procedure_controller;

})();