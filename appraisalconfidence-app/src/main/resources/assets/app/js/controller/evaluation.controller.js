(function() {

    evaluation_controller.$inject = [
            '$scope',
            '$stateParams',
            '$http',
            'appcon'
        ];

    require('bootstrap-slider');

    function evaluation_controller($scope, $stateParams, $http, appcon) {
        $('#slider1').slider();
        $('#slider1').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.absConfidence = val;
            $('#slider1Val').text(val);
        });

        $('#slider2').slider();
        $('#slider2').on('slide', function(slideEvt) {
            var val = slideEvt.value;
            $scope.relConfidence = val;
            $('#slider2Val').text(val);
        });

        $scope.selectedEvaluation = '';
        $scope.selectedEvaluationTitle = '';

        appcon.getReviews($stateParams.id).then(
            function(response) {
                console.log('GET /api/performance-review/ ' + response.status);
                $scope.evaluations = response.data;
                $scope.$parent.stopSpinner();
            },
            function(response) {
                alert('Error: Unable to retrieve teacher evaluations');
                console.log('GET /api/performance-review/ ' + response.status);
                console.log(response);
                $scope.$parent.stopSpinner();
            }
        );

        // Comment control
        var comment_max_length = 200;
        $scope.comment = '';
        $scope.remainingChars = 200;
        $scope.calculateRemainingChars = function() {
            $scope.remainingChars = comment_max_length - $scope.comment.length;
        }

        $scope.saveAndContinue = function() {
            $scope.$parent.startSpinner();
            $timeout(function() {
                $scope.$parent.stopSpinner();
            }, 5000);
        }

        $scope.back = function() {
            alert('The back button is not available yet!');
        }

    };


    module.exports = evaluation_controller;

})();