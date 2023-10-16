(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('TypeActivite', TypeActivite);

    TypeActivite.$inject = ['$resource'];

    function TypeActivite ($resource) {
        var resourceUrl =  'api/type-activites/:id';

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
