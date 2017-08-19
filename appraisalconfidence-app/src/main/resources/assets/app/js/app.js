(function() {

    var angular = require('angular');
//    var auth_vars = require('./auth0-vars.js');
//    require('auth0-js');
    require('@uirouter/angularjs');
//    require('angular-auth0');
// ['auth0.auth0', 'ui.router']
    var appraisal_app = angular.module('appraisal_app', ['ui.router']);
    // Configure app
    appraisal_app.config(app_config);

    app_config.$inject = [
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider'
//        'angularAuth0Provider'
    ];

    function app_config($stateProvider, $locationProvider, $urlRouterProvider, angularAuth0Provider) {
        // Configure state provider for UI routes
        $stateProvider
          .state('home', {
            url: '/home',
            controller: 'home-cntr',
            templateUrl: 'app/template/home.html',
            controllerAs: 'vm'
          })
          .state('callback', {
            url: '/callback',
            controller: 'callback-cntr',
            templateUrl: 'app/template/callback.html',
            controllerAs: 'vm'
          });

          // Configure auth provider
//          angularAuth0Provider.init({
//              clientID: auth_vars.clientID,
//              domain: auth_vars.domain,
//              responseType: 'token id_token',
//              audience: 'https://appraisal-grenoble-bourji.auth0.com/userinfo',
//              redirectUri: 'http://localhost:5000/#/callback',
//              scope: 'openid profile'
//          });

          $urlRouterProvider.otherwise('/');
          $locationProvider.hashPrefix('');
    }

    // Register app controllers
    appraisal_app.controller('home-cntr', require('./controller/home.controller.js'));
    appraisal_app.controller('callback-cntr', require('./controller/callback.controller.js'));
    // Register app services
//    appraisal_app.service('auth-svc', require('./service/auth.service.js'));
    // Register app directives
//    appraisal_app.directive('navbar', require('./directive/navbar.directive.js'));
})();