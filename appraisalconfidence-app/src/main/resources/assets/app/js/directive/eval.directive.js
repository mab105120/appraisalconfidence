(function() {

    'use strict';

    function eval_directive() {
        return {
            restrict: 'E',
            scope: {
                jobFunction: '@',
                jobFunctionCode: '@'
            },
            templateUrl: 'app/template/eval.html',
            link: function(scope) {
                scope.displayEvaluation = function(teacher, supervisor) {

                    var teachers = {
                        T1: 'Teacher 1',
                        T2: 'Teacher 2'
                    }

                    var supervisors = {
                        SP1: 'Supervisor 1',
                        SP2: 'Supervisor 2',
                        SP3: 'Supervisor 3'
                    }

                    scope.$parent.time_modal_open = new Date().toISOString();
                    scope.$parent.selectedEvaluationCode = scope.jobFunctionCode + '-' + teacher + '-' + supervisor;
                    scope.$parent.selectedEvaluation = scope.$parent.evaluations[teacher][scope.jobFunctionCode][supervisor];
                    scope.$parent.selectedEvaluationTitle = "This is what " + supervisors[supervisor] + " had to say about " + teachers[teacher] + "'s "+scope.jobFunction+"  skills";
                }
            }
        };
    }

    module.exports = eval_directive;
})();