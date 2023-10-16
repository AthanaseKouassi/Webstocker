(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LigneobjectifventeSearch', LigneobjectifventeSearch);

    LigneobjectifventeSearch.$inject = ['$resource'];

    function LigneobjectifventeSearch($resource) {
        var resourceUrl =  'api/_search/ligneobjectifventes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
