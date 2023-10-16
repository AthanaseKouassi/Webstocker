(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('CelluleSearch', CelluleSearch);

    CelluleSearch.$inject = ['$resource'];

    function CelluleSearch($resource) {
        var resourceUrl =  'api/_search/cellules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
