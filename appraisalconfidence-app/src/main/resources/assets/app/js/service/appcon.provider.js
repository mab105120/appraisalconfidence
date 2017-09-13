(function() {

    function appcon_provider() {
        var url;

        this.setUrl = function(val) {
            url = val;
        }

        appcon_service.$inject = ['$http', 'authService'];

        function appcon_service($http, authService) {

            function getReviews(evaluationCode) {
                var config = {
                    method: 'GET',
                    url:  url + '/api/performance-review/' + evaluationCode
                };
                return $http(config);
            }

            function postUserDemographic(user) {
                var access_token = localStorage.getItem("id_token");
                return $http({
                    method: 'POST',
                    headers: {
                        "Authorization": 'Bearer ' + access_token
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-demographic'
                });
            }

            function postUserExperience(user) {
                var id_token = localStorage.getItem('id_token');
                return $http({
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + id_token
                    },
                    data: user,
                    url: url + '/api/questionnaire/user-experience'
                });
            }

            return {
                getReviews: getReviews,
                postUserDemographic: postUserDemographic,
                postUserExperience: postUserExperience
            }
        };

        this.$get = appcon_service;
    }

    module.exports = appcon_provider;
})();