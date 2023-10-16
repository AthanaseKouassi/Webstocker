(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Magasin', Magasin);

    Magasin.$inject = ['$resource'];

    function Magasin ($resource) {
        var resourceUrl =  'api/magasins/:id';

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
