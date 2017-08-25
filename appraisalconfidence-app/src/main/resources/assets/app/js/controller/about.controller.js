(function() {

    about_controller.$inject = [
        '$scope',
        'authService',
        '$state'
    ];

    function about_controller($scope, authService, $state) {

        // check if user is authenticated
        if(!authService.isAuthenticated()) {
            alert('You are not logged in! please log in to access this page.');
            $state.go('home');
        }

        $scope.aboutUs = 'here is some information about us!';
    };

    module.exports = about_controller;

})();