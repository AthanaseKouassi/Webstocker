(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LignecommandeSearch', LignecommandeSearch);

    LignecommandeSearch.$inject = ['$resource'];

    function LignecommandeSearch($resource) {
        var resourceUrl =  'api/_search/lignecommandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
