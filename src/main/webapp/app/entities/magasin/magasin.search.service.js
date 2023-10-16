(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('MagasinSearch', MagasinSearch);

    MagasinSearch.$inject = ['$resource'];

    function MagasinSearch($resource) {
        var resourceUrl =  'api/_search/magasins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
