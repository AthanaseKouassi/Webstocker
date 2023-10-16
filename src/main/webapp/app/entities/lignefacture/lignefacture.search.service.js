(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LignefactureSearch', LignefactureSearch);

    LignefactureSearch.$inject = ['$resource'];

    function LignefactureSearch($resource) {
        var resourceUrl =  'api/_search/lignefactures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
