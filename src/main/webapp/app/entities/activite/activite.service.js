(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Activite', Activite);

    Activite.$inject = ['$resource'];

    function Activite ($resource) {
        var resourceUrl =  'api/activites/:id';

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
