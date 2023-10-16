(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('FabricantSearch', FabricantSearch);

    FabricantSearch.$inject = ['$resource'];

    function FabricantSearch($resource) {
        var resourceUrl =  'api/_search/fabricants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
