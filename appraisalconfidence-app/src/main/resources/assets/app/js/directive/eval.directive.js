(function() {

    'use strict';

    function eval_directive() {
        return {
            restrict: 'E',
            scope: {
                jobFunction: '@'
            },
            templateUrl: 'app/template/eval.html',
            link: function(scope) {
                scope.displayEvaluation = function(teacher, supervisor) {

                    function getSupervisorName(supervisor) {
                        if(supervisor.includes('1')) return 'Supervisor 1';
                        else if (supervisor.includes('2')) return 'Supervisor 2';
                        else if(supervisor.includes('3')) return 'Supervisor 3';
                        else return null;
                    }

                    function getTeacherName(teacher) {
                        if(teacher.includes('1')) return 'Teacher 1';
                        else if (teacher.includes('2')) return 'Teacher 2';
                        else return null;
                    }

                    function normalizeJobFunction(jobTitle) {
                        switch(jobTitle) {
                            case 'Student Learning':
                                return 'studentLearning';
                            case 'Instructional Practice':
                                return 'instructionalPractice';
                            case 'Professionalism':
                                return 'professionalism';
                            default:
                                return null; // TODO handle this case
                        }
                    }
                    scope.$parent.time_modal_open = new Date().toISOString();
                    scope.$parent.selectedEvaluationCode = scope.jobFunction + '-' + teacher + '-' + supervisor;
                    scope.$parent.selectedEvaluation = scope.$parent.evaluations[teacher][normalizeJobFunction(scope.jobFunction)][supervisor];
                    scope.$parent.selectedEvaluationTitle = "This is what " + getSupervisorName(supervisor) + " had to say about "+getTeacherName(teacher)+"'s "+scope.jobFunction+"  skills";
                }
            }
        };
    }

    module.exports = eval_directive;
})();