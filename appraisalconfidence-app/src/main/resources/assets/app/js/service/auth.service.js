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
                    alert('An error occured while trying to parse the URL has. Please see console for more details!');
                    console.log('error details: ' + err);
               }
            });
        }

        function setSession(authResult) {
            console.log('Setting authentication result to local storage');
            let expiresAt = JSON.stringify((authResult.expiresIn * 1000) + new Date().getTime());
            localStorage.setItem('access_token', authResult.accessToken);
            localStorage.setItem('id_token', authResult.idToken);
            localStorage.setItem('expires_at', expiresAt);
        }

        function logout() {
            console.log('user is being logged out!');
            localStorage.removeItem('access_token');
            localStorage.removeItem('id_token');
            localStorage.removeItem('expires_at');
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