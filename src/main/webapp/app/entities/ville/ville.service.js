(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Ville', Ville);

    Ville.$inject = ['$resource'];

    function Ville ($resource) {
        var resourceUrl =  'api/villes/:id';

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
