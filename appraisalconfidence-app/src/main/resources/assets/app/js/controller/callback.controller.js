(function() {

    'use-strict';

    callback_controller.$inject = ['$state']

    function callback_controller($state) {
        // implement controller
        if(localStorage.getItem('redirect_state') === null)
            $state.go('home');
        else {
            $state.go(localStorage.getItem('redirect_state'));
            localStorage.removeItem('redirect_url');
        }
    };

    module.exports = callback_controller;
})();