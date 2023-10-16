(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('ObjectifsSearch', ObjectifsSearch);

    ObjectifsSearch.$inject = ['$resource'];

    function ObjectifsSearch($resource) {
        var resourceUrl =  'api/_search/objectifs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
