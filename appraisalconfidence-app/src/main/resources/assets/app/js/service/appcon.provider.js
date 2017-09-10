(function() {

    function appcon_provider() {
        var url;

        this.setUrl = function(val) {
            url = val;
        }

        appcon_service.$inject = ['$http'];

        function appcon_service($http) {

            function getReviews(evaluationCode) {
                var config = {
                    method: 'GET',
                    url:  url + '/api/performance-review/' + evaluationCode
                };
                return $http(config);
            }

            function postUserDemographic(user) {
                return $http({
                    method: 'POST',
                    url: url + '/api/questionnaire/user-demographic'
                });
            }

            return {
                getReviews: getReviews,
                postUserDemographic: postUserDemographic
            }
        };

        this.$get = appcon_service;
    }

    module.exports = appcon_provider;
})();