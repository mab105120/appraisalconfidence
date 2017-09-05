(function() {

    $ = jQuery = require('jquery');
    require('bootstrap');
    var angular = require('angular');
    require('@uirouter/angularjs');
    require('angular-auth0');
    require('angular-spinner');

    var appraisal_app = angular.module('appraisal_app', require('./app-modules.js'));
    // Configure app
    appraisal_app.config(require('./app-config'));

    // Register app controller
    appraisal_app.controller('mainController', require('./controller/main.controller.js'));
    appraisal_app.controller('homeController', require('./controller/home.controller.js'));
    appraisal_app.controller('callbackController', require('./controller/callback.controller.js'));
    appraisal_app.controller('welcomeController', require('./controller/welcome.controller.js'));
    appraisal_app.controller('procedureController', require('./controller/procedure.controller.js'));
    appraisal_app.controller('quesGeneralController', require('./controller/questionnaire/general.controller.js'));
    appraisal_app.controller('quesExperienceController', require('./controller/questionnaire/experience.controller.js'));
    appraisal_app.controller('questConfidenceController', require('./controller/questionnaire/confidence.controller.js'));
    appraisal_app.controller('evaluationController', require('./controller/evaluation.controller.js'));

    // Register app services
    appraisal_app.service('authService', require('./service/auth.service.js'));

    // Register app directives
    appraisal_app.directive('navbar', require('./directive/navbar.directive.js'));
    appraisal_app.directive('eval', require('./directive/eval.directive.js'));
    appraisal_app.directive('focusOnShow', require('./directive/focusonshow.directive.js'));
    appraisal_app.directive('likert', require('./directive/likert.directive.js'));

    // Handle authentication when application runs
    appraisal_app.run(run);

    run.$inject = ['authService'];

    function run(authService) {
        console.log('Handling authentication');
        authService.handleAuthentication();
    }
})();