(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Bailleur', Bailleur);

    Bailleur.$inject = ['$resource'];

    function Bailleur ($resource) {
        var resourceUrl =  'api/bailleurs/:id';

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
