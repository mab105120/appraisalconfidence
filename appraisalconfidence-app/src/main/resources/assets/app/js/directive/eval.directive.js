(function() {

    'use strict';

    function eval_directive() {
        return {
            restrict: 'EAC',
            scope: {
                jobFunction: '@'
            },
            templateUrl: 'app/template/eval.html',
            link: function(scope) {
                scope.displayEvaluation = function(teacher, supervisor) {
                    console.log(scope);
                    scope.$parent.selectedEvaluation = scope.$parent.evaluations[teacher][scope.jobFunction][supervisor];
                }
            }
        };
    }

    module.exports = eval_directive;
})();