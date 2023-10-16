(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('BailleurSearch', BailleurSearch);

    BailleurSearch.$inject = ['$resource'];

    function BailleurSearch($resource) {
        var resourceUrl =  'api/_search/bailleurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
