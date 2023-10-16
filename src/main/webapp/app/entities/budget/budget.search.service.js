(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('BudgetSearch', BudgetSearch);

    BudgetSearch.$inject = ['$resource'];

    function BudgetSearch($resource) {
        var resourceUrl =  'api/_search/budgets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
