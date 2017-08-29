(function() {

    'use strict';

    function eval_directive() {
        return {
            restrict: 'EAC',
            scope: {
                jobFunction: '@',
                evaluations: '='
            },
            templateUrl: 'app/template/eval.html',
            link: function(scope, element, attributes) {
                scope.setModalBody = function(teacher, jobFunction, supervisor) {
                    console.log('Evaluation report requested for: ' + teacher + ' ' + jobFunction + ' ' + supervisor);
                    scope.modalBody = scope.evaluations[teacher][jobFunction][supervisor];
                };
            }
        };
    }

    module.exports = eval_directive;
})();