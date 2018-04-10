(function() {

    'use strict';

    main_controller.$inject = ['$scope', 'usSpinnerService', '$location'];

    function main_controller($scope, usSpinnerService, $location) {

        require('block-ui');

        $scope.startSpinner = function() {
            $.blockUI({ message: null });
            usSpinnerService.spin('main-spinner');
        }
        $scope.stopSpinner = function() {
            $.unblockUI();
            usSpinnerService.stop('main-spinner');
        }

        if(localStorage.getItem('qual_redirect_url') === null) {
            var qual_redirect_url = 'https://quninq9ktegh7vsv.co1.qualtrics.com/jfe/preview/SV_abhZZ1YMY2YotCZ?Q_CHL=preview';
            var params = $location.search();
            for(var param in params) {
                if(params.hasOwnProperty(param))
                    qual_redirect_url += '&' + param + '=' + params[param];
            }
            localStorage.setItem('qual_redirect_url', qual_redirect_url);
        }

    }

    module.exports = main_controller;

})();