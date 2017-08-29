(function() {

    evaluation_controller.$inject = ['$scope'];

    require('bootstrap-slider');

    function evaluation_controller($scope) {
        console.log('registering the slider');
        $('#slider1').slider();
        $('#slider1').on('slide', function(slideEvt) {
            $('#slider1Val').text(slideEvt.value);
        });

        $('#slider2').slider();
        $('#slider2').on('slide', function(slideEvt) {
            $('#slider2Val').text(slideEvt.value);
        });

        // When this controller init it should create this object
        $scope.teacherReviews = {
            "teacher1": {
                "teachingSkills": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 1 teaching skills",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 1 teaching skills",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 1 teaching skills"
                },
                "communication": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 1 communication skills",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 1 communication skills",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 1 communication skills"
                },
                "professionalism": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 1 professionalism",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 1 professionalism",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 1 professionalism"
                }
            },
            "teacher2": {
                "teachingSkills": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 2 teaching skills",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 2 teaching skills",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 2 teaching skills"
                },
                "communication": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 2 communication skills",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 2 communication skills",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 2 communication skills"
                },
                "professionalism": {
                    "supervisor1": "This is what supervisor 1 thinks about teacher 2 professionalism",
                    'supervisor2': "This is what supervisor 2 thinks about teacher 2 professionalism",
                    'supervisor3': "This is what supervisor 3 thinks about teacher 2 professionalism"
                }
            }
        };

        $scope.jobFunctions = [
            'teachingSkills',
            'communication',
            'professionalism'
        ];
    };


    module.exports = evaluation_controller;

})();