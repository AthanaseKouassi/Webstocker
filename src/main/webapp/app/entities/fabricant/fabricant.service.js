(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Fabricant', Fabricant);

    Fabricant.$inject = ['$resource'];

    function Fabricant ($resource) {
        var resourceUrl =  'api/fabricants/:id';

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
