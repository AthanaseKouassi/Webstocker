(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LigneBudgetSearch', LigneBudgetSearch);

    LigneBudgetSearch.$inject = ['$resource'];

    function LigneBudgetSearch($resource) {
        var resourceUrl =  'api/_search/ligne-budgets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
