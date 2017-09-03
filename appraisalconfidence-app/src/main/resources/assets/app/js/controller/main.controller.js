(function() {

    'use strict';

    main_controller.$inject = ['$scope', 'usSpinnerService'];

    function main_controller($scope, usSpinnerService) {

        require('block-ui');

        $scope.showAlert = false;
        $scope.setAlert = function(title, body) {
            $scope.alertTitle = title;
            $scope.alertBody = body;
            $scope.showAlert = true;
        }

        $scope.hideAlert = function() {
            $scope.showAlert = false;
        }

        $scope.startSpinner = function() {
            $.blockUI({ message: null });
            usSpinnerService.spin('main-spinner');
        }
        $scope.stopSpinner = function() {
            $.unblockUI();
            usSpinnerService.stop('main-spinner');
        }
    }

    module.exports = main_controller;

})();