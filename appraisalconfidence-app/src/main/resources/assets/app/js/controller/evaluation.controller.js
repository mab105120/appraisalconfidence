(function() {

    evaluation_controller.$inject = ['$scope', '$stateParams', '$http', '$timeout'];

    require('bootstrap-slider');

    function evaluation_controller($scope, $stateParams, $http, $timeout) {
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

        function getTeacherEvaluation() {
            $scope.$parent.startSpinner();
            var config = {
                method: 'GET',
                url:  'http://localhost:5000/api/appraisal/evaluations/' + $stateParams.id
            };
            $http(config)
                .then(
                    function(response) {
                        console.log('GET /api/appraisal/evaluations/ ' + response.status);
                        $scope.evaluations = response.data;
                        $scope.$parent.stopSpinner();
                    },
                    function(response) {
                        $scope.$parent.setAlert('Error', 'Unable to retrieve teacher evaluations');
                        console.log('GET /api/appraisal/evaluations/ ' + response.status);
                        console.log(response);
                        $scope.$parent.stopSpinner();
                    }
                );
        }

        getTeacherEvaluation();

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
            console.log('The back button is clicked!');
            $scope.$parent.setAlert('Test', 'The back button us not available now!');
        }

    };


    module.exports = evaluation_controller;

})();