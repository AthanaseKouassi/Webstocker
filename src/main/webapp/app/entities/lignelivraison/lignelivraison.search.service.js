(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LignelivraisonSearch', LignelivraisonSearch);

    LignelivraisonSearch.$inject = ['$resource'];

    function LignelivraisonSearch($resource) {
        var resourceUrl =  'api/_search/lignelivraisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
