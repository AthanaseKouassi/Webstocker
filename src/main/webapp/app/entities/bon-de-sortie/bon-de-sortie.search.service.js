(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('BonDeSortieSearch', BonDeSortieSearch);

    BonDeSortieSearch.$inject = ['$resource'];

    function BonDeSortieSearch($resource) {
        var resourceUrl =  'api/_search/bon-de-sorties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
