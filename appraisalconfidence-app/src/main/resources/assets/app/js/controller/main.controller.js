(function() {

    'use strict';

    main_controller.$inject = ['$scope', 'usSpinnerService'];

    function main_controller($scope, usSpinnerService) {

        require('block-ui');

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