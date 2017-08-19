(function() {

    'use strict';

    home_controller.$inject = [
            '$scope',
            '$http',
            'authService'
        ];

    function home_controller($scope, $http, authService) {
        $scope.login = function() {
            authService.login();
        };

        $scope.test = function() {
            $http.get('https://localhost:5000/api/appraisal/test')
            .then(function(res) {
                alert(res.data);
            }, function(err) {
                alert('error occurred when making rest call. see console for details');
                console.log(err);
            });
        }
    };

    module.exports = home_controller;
});