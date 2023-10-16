(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Conditionnement', Conditionnement);

    Conditionnement.$inject = ['$resource'];

    function Conditionnement ($resource) {
        var resourceUrl =  'api/conditionnements/:id';

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
