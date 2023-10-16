(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('LigneBudget', LigneBudget);

    LigneBudget.$inject = ['$resource'];

    function LigneBudget ($resource) {
        var resourceUrl =  'api/ligne-budgets/:id';

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
