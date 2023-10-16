(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LotSearch', LotSearch);

    LotSearch.$inject = ['$resource'];

    function LotSearch($resource) {
        var resourceUrl =  'api/_search/lots/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
