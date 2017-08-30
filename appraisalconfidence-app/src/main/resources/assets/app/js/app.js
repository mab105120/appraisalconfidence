(function() {

    $ = jQuery = require('jquery');
    require('bootstrap');
    var angular = require('angular');
    var auth_vars = require('./auth0-vars.js');
    require('@uirouter/angularjs');
    require('angular-auth0');
    var appraisal_app = angular.module('appraisal_app', ['auth0.auth0','ui.router']);
    // Configure app
    appraisal_app.config(app_config);

    app_config.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        'angularAuth0Provider'
    ];

    function app_config($stateProvider, $locationProvider, $urlRouterProvider, angularAuth0Provider) {
        // Configure state provider for UI routes
        $stateProvider
          .state('welcome', {
             url: '/',
             controller: 'welcomeController',
             templateUrl: 'app/template/welcome.html'
          })
          .state('home', {
             url: '/home',
             controller: 'homeController',
             templateUrl: 'app/template/home.html'
           })
          .state('callback', {
            url: '/callback',
            controller: 'callbackController',
            templateUrl: 'app/template/callback.html'
          })
          .state('procedure', {
            url: '/procedure',
            controller: 'procedureController',
            templateUrl: 'app/template/procedure.html'
          })
          .state('evaluation', {
            url: '/evaluation/:id',
            controller: 'evaluationController',
            templateUrl: 'app/template/evaluation.html'
          });

//           Configure auth provider
          angularAuth0Provider.init({
              clientID: auth_vars.clientID,
              domain: auth_vars.domain,
              responseType: 'token id_token',
              audience: 'https://appraisal-grenoble-bourji.auth0.com/userinfo',
              redirectUri: 'http://localhost:5000/#/callback',
              scope: 'openid profile'
          });

//          $locationProvider.html5Mode(true);
          $urlRouterProvider.otherwise('/');
          $locationProvider.hashPrefix('');
    }

    // Register app controller
    appraisal_app.controller('homeController', require('./controller/home.controller.js'));
    appraisal_app.controller('callbackController', require('./controller/callback.controller.js'));
    appraisal_app.controller('aboutController', require('./controller/about.controller.js'));
    appraisal_app.controller('welcomeController', require('./controller/welcome.controller.js'));
    appraisal_app.controller('procedureController', require('./controller/procedure.controller.js'));
    appraisal_app.controller('evaluationController', require('./controller/evaluation.controller.js'));
    // Register app services
    appraisal_app.service('authService', require('./service/auth.service.js'));
    // Register app directives
    appraisal_app.directive('navbar', require('./directive/navbar.directive.js'));
    appraisal_app.directive('eval', require('./directive/eval.directive.js'));
    // Handle authentication when application runs
    appraisal_app.run(run);

    run.$inject = ['authService'];

    function run(authService) {
        console.log('Handling authentication');
        authService.handleAuthentication();
    }
})();