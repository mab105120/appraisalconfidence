(function(){

    tenure_controller.$inject = [
        '$scope',
        '$state',
        '$window'
    ]

    function tenure_controller($scope, $state, $window) {
        $scope.submit = function() {
            $window.scrollTo(0, 0);
            $state.go('evaluation', {id: 1});
        }
    }

    module.exports = tenure_controller;
})();