(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('InventaireSearch', InventaireSearch);

    InventaireSearch.$inject = ['$resource'];

    function InventaireSearch($resource) {
        var resourceUrl =  'api/_search/inventaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
