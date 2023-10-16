(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LigneBonDeSortieSearch', LigneBonDeSortieSearch);

    LigneBonDeSortieSearch.$inject = ['$resource'];

    function LigneBonDeSortieSearch($resource) {
        var resourceUrl =  'api/_search/ligne-bon-de-sorties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
