(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Cellule', Cellule);

    Cellule.$inject = ['$resource'];

    function Cellule ($resource) {
        var resourceUrl =  'api/cellules/:id';

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
