(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Categorieclient', Categorieclient);

    Categorieclient.$inject = ['$resource'];

    function Categorieclient ($resource) {
        var resourceUrl =  'api/categorieclients/:id';

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
