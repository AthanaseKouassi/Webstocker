(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LivraisonSearch', LivraisonSearch);

    LivraisonSearch.$inject = ['$resource'];

    function LivraisonSearch($resource) {
        var resourceUrl =  'api/_search/livraisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
