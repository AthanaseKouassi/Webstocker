(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Ligneobjectifvente', Ligneobjectifvente);

    Ligneobjectifvente.$inject = ['$resource'];

    function Ligneobjectifvente ($resource) {
        var resourceUrl =  'api/ligneobjectifventes/:id';

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
