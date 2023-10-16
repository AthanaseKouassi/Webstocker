(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Lignelivraison', Lignelivraison);

    Lignelivraison.$inject = ['$resource'];

    function Lignelivraison ($resource) {
        var resourceUrl =  'api/lignelivraisons/:id';

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
