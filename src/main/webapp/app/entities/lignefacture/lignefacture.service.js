(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Lignefacture', Lignefacture);

    Lignefacture.$inject = ['$resource'];

    function Lignefacture ($resource) {
        var resourceUrl =  'api/lignefactures/:id';

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
