(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('PrixSearch', PrixSearch);

    PrixSearch.$inject = ['$resource'];

    function PrixSearch($resource) {
        var resourceUrl =  'api/_search/prixes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
