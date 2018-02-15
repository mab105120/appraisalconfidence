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
                    scope.$parent.modalCode = scope.jobFunctionCode + '-' + teacher + '-' + supervisor;
                    scope.$parent.modalBody = scope.$parent.evaluations[teacher][scope.jobFunctionCode][supervisor];
                    scope.$parent.modalTitle = "This is what " + supervisors[supervisor] + " had to say about " + teachers[teacher] + "'s "+scope.jobFunction+"  skills";
                }

                scope.displayJobFunctionDetails = function(jobFunction, jobFunctionCode) {

                    var jobFunctionDetails = {
                        SL: {
                                header: 'Tenure applicants must provide consistent evidence showing that all students, including those with special needs, perform competitively on state standard exams for academic qualifications.',
                                details: [
                                ]
                            },
                        IP: {
                            header: 'Tenure applicants must provide consistent evidence indicating practice at the most effective level in the categories below:',
                            details: [
                                'Planning and preparation',
                                'Classroom environment',
                                'Instruction'
                            ]
                        },
                        PF: {
                            header: 'Tenure applicants must provide consistent evidence of high level professionalism on activities including:',
                            details: [
                                'Professional growth and reflection',
                                'Collaboration and engagement with the school community',
                                'Effective communication with students’ families',
                                'Management of non-instructional responsibilities',
                                'Professional conduct'
                            ]
                        }
                    }

                    var getModalBody = function(jobFunctionCode) {
                        var body = '';
                        var detailsObj =  jobFunctionDetails[jobFunctionCode];
                        body = body + '<p>' + detailsObj['header'] + '</p>';
                        if (detailsObj.details.length != 0) {
                            body = body + '<ul>';
                            angular.forEach(detailsObj.details, function(item) {
                                body = body + '<li>' + item + '</li>';
                            });
                            body = body + '</ul>';
                        }
                        return body;
                    }

                    scope.$parent.time_modal_open = new Date().toISOString();
                    scope.$parent.modalCode = 'D-' + jobFunctionCode;
                    scope.$parent.modalTitle = jobFunction;
                    scope.$parent.modalBody = getModalBody(jobFunctionCode);
                }
            }
        };
    }

    module.exports = eval_directive;
})();