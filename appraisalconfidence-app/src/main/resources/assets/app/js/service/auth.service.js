(function() {

    'use-strict';

    authService.$inject = [
        '$state',
        'angularAuth0',
        '$timeout'
    ]

    function authService($state, angularAuth0, $timeout) {

        function login() {
            // remember current state to reroute to after authentication
            if ($state.current.name === 'welcome')
                localStorage.setItem('redirect_state', 'home');
            else
                localStorage.setItem('redirect_state', $state.current.name);

            angularAuth0.authorize();
        }

        function handleAuthentication() {
            angularAuth0.parseHash(function(err, authResult) {
               if(authResult && authResult.accessToken && authResult.idToken) {
                    setSession(authResult);
                    if (localStorage.getItem('redirect_state') === null)
                        $state.go('home');
                    else {
                        $state.go(localStorage.getItem('redirect_state'));
                        localStorage.removeItem('redirect_state');
                    }
               } else if (err) {
                    alert('An error occurred while trying to parse the URL has. Please see console for more details!');
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
            $state.go('welcome');
        }


        function isAuthenticated() {
            console.log('isAuthenticated is executed');
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