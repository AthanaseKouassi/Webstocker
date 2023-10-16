(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Ligneprixproduit', Ligneprixproduit);

    Ligneprixproduit.$inject = ['$resource'];

    function Ligneprixproduit ($resource) {
        var resourceUrl =  'api/ligneprixproduits/:id';

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
