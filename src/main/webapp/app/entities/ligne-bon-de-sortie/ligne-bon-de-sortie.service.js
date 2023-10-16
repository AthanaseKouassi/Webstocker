(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('LigneBonDeSortie', LigneBonDeSortie);

    LigneBonDeSortie.$inject = ['$resource'];

    function LigneBonDeSortie ($resource) {
        var resourceUrl =  'api/ligne-bon-de-sorties/:id';

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
