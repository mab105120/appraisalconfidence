(function() {

    contactUs_controller.$inject = [
        '$scope',
        'authService',
        'appcon',
        'toaster'
    ]

    function contactUs_controller($scope, authService, appcon, toast) {
        init();
        function init() {
            $scope.subject = '';
            $scope.body = '';
            $scope.email = authService.getUserId();
        }

        $scope.submit = function() {
            $scope.$parent.startSpinner();

            appcon.sendEmail($scope.email, $scope.subject, $scope.body)
            .then(function success() {
                toast.pop('success', 'Sent!', 'Your email has been sent. We will reach out to you ASAP!');
                $scope.$parent.stopSpinner();
            }, function failure() {
                var error = response.data === null ? 'Server unreachable' : response.data.message;
                toaster.pop('error', 'Error', 'Oops! we were not able to send the email: ' + error);
                console.log('Error Object');
                console.log(response);
                $scope.$parent.stopSpinner();
            });
        }
    }

    module.exports = contactUs_controller;

})();