(function() {

    var auth_vars = require('./auth0-vars.js');

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

          // Configure auth provider
          angularAuth0Provider.init({
              clientID: auth_vars.clientID,
              domain: auth_vars.domain,
              responseType: 'token id_token',
              audience: 'https://appraisal-grenoble-bourji.auth0.com/userinfo',
              redirectUri: 'http://localhost:5000/#/callback',
              scope: 'openid profile'
          });

          $urlRouterProvider.otherwise('/');
          $locationProvider.hashPrefix('');
    }

    module.exports = app_config;
})();