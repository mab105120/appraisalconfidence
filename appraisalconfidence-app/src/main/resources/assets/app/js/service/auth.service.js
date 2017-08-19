(function() {

    'use-strict';

    authService.$inject = [
        '$state',
        'angularAuth0',
        '$timeout'
    ]

    function authService($state, angularAuth0, $timeout) {

        function login() {
            angularAuth0.authorize();
        }

        function handleAuthentication() {
            angularAuth0.parseHash(function(err, authResult) {
               if(authResult && authResult.accessToken && authResult.idToken) {
                    setSession(authResult);
                    $state.go('home');
               } else if (err) {
                    alert('an error occurred. check console for details');
                    console.log('error details: ' + err);
               }
            });
        }

        function setSession(authResult) {
            let expiresAt = JSON.stringify((authResult.expiresIn * 1000) + new Date().getTime());
            localStorage.setItem('access_token', authResult.accessToken);
            localStorage.setItem('id_token', authResult.idToken);
            localStorage.setItem('expires_at', expiresAt);
        }

        function logout() {
            localStorage.remove('access_token');
            localStorage.remove('id_token');
            localStorage.remove('expires_at');
        }


        function isAuthenticated() {
            let expiresAt = JSON.parse(localStorage.getItem('expires_at'));
            return new Date().getTime() < expiresAt;
        }

        return {
            login: login,
            handleAuthentication: handleAuthentication,
            logout: logout,
            isAuthenticated: isAuthenticated
        }
    };

    module.exports = authService;
})();