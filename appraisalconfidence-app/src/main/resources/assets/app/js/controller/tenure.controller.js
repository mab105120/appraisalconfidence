(function(){

    tenure_controller.$inject = [
        '$scope',
        '$state'
    ]

    function tenure_controller($scope, $state) {
        $scope.submit = function() {
            $state.go('evaluation', {id: 1});
        }
    }

    module.exports = tenure_controller;
})();