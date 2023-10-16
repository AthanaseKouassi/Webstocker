(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('ActiviteSearch', ActiviteSearch);

    ActiviteSearch.$inject = ['$resource'];

    function ActiviteSearch($resource) {
        var resourceUrl =  'api/_search/activites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
