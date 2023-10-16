(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Budget', Budget);

    Budget.$inject = ['$resource'];

    function Budget ($resource) {
        var resourceUrl =  'api/budgets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
