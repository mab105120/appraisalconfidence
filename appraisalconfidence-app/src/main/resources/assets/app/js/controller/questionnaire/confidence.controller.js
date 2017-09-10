(function() {

    quest_confidence_controller.$inject = [
        '$scope',
        '$state'
    ];

    function quest_confidence_controller($scope, $state) {
        $scope.submit = function() {
            console.log($scope.items);
            console.log($scope.items2);
        }

        $scope.choices = [
            {
                choice: 'Strongly Disagree',
                value: -2
            },
            {
                choice: 'Disagree',
                value: -1
            },
            {
                choice: 'Neutral',
                value: 0
            },
            {
                choice: 'Agree',
                value: 1
            },
            {
                choice: 'Strongly Agree',
                value: 2
            }
        ];

        $scope.items = [
            {
                item: 'Often I put off making difficult decisions',
                code: 'jsd1'
            },
            {
                item: "I often don't trust myself to make the right decision",
                code: 'jsd2'
            },
            {
                item: "I often trust the judgment of others more than my own",
                code: 'jsd3'
            },
            {
                item: "In almost all situations I am confident of my ability to make the right choices",
                code: 'jsd4'
            },
            {
                item: "Frequently, I doubt my ability to make sound judgments",
                code: 'jsd5'
            },
            {
                item: "I often worry about whether a decision I made will have bad consequences",
                code: 'jsd6'
            },
            {
                item: "When making a decision, I often feel confused because I have trouble keeping all relevant factors in mind",
                code: 'jsd7'
            },
            {
                item: "I have a great deal of confidence in my opinions",
                code: 'jsd8'
            }
        ];
        $scope.items2 = [
            {
                item: 'I tend to struggle with most decisions',
                code: 'pfi1'
            },
            {
                item: 'Even after making an important decision I tend to continue to think about the pros and cons to make sure that I am not wrong',
                code: 'pfi2'
            },
            {
                item: 'I rarely doubt that the course of action I have selected be correct',
                code: 'pfi3'
            },
            {
                item: 'I tend to continue to evaluate recently made decisions',
                code: 'pfi4'
            },
            {
                item: 'Decisions rarely weigh heavily on my shoulders',
                code: 'pfi5'
            },
        ];

        $scope.submit = function() {
            $state.go('procedure');
        }
    }

    module.exports = quest_confidence_controller;
})();