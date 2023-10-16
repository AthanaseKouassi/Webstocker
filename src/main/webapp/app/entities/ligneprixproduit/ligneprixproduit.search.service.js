(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LigneprixproduitSearch', LigneprixproduitSearch);

    LigneprixproduitSearch.$inject = ['$resource'];

    function LigneprixproduitSearch($resource) {
        var resourceUrl =  'api/_search/ligneprixproduits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
