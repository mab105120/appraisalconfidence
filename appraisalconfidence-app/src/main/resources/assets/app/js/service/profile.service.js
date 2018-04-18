(function() {

    'use-strict';

    function profile_service() {
        function getProfile() {
            return {
                name: 'profile1',
                isRelative: false,
                practice: 3,
                feedback: 'lo',
                totalEvaluations: 4,
                duration: 30
            };
        }

        return {
            getProfile: getProfile
        }
    }

    module.exports = profile_service;

})();