(function() {

    evaluation_controller.$inject = ['$scope', '$stateParams', '$http'];

    require('bootstrap-slider');

    function evaluation_controller($scope, $stateParams, $http) {
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

        $http.get('http://localhost:5000/api/appraisal/evaluations/' + $stateParams.id)
        .then(
            function(response) {
                console.log('GET /api/appraisal/evaluations/ ' + response.status);
                $scope.evaluations = response.data;
            },
            function(response) {
//                alert('Unable to contact the server for reviews');
                console.log('GET /api/appraisal/evaluations/ ' + response.status);
                console.log(response);
            }
        );

        // Comment control
        var comment_max_length = 200;
        $scope.comment = '';
        $scope.remainingChars = 200;
        $scope.calculateRemainingChars = function() {
            $scope.remainingChars = comment_max_length - $scope.comment.length;
        }

        // Continue button
        $scope.continueBtnDisabled = function() {
            if($scope.comment.length === 0) return true;
            else
            return true;
        };
    };


    module.exports = evaluation_controller;

})();