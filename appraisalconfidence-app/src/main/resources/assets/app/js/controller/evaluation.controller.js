(function() {

    evaluation_controller.$inject = ['$scope', '$stateParams', '$http'];

    require('bootstrap-slider');

    function evaluation_controller($scope, $stateParams, $http) {
        $('#slider1').slider();
        $('#slider1').on('slide', function(slideEvt) {
            $('#slider1Val').text(slideEvt.value);
        });

        $('#slider2').slider();
        $('#slider2').on('slide', function(slideEvt) {
            $('#slider2Val').text(slideEvt.value);
        });

        $scope.selectedEvaluation = '';

        $http.get('http://localhost:5000/api/appraisal/evaluations/' + $stateParams.id)
        .then(
            function(response) {
                console.log('GET /api/appraisal/evaluations/ ' + response.status);
                $scope.evaluations = response.data;
            },
            function(response) {
                console.log('GET /api/appraisal/evaluations/ ' + response.status);
                console.log(response);
            }
        );
    };


    module.exports = evaluation_controller;

})();