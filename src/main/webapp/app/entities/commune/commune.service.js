(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Commune', Commune);

    Commune.$inject = ['$resource'];

    function Commune ($resource) {
        var resourceUrl =  'api/communes/:id';

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
