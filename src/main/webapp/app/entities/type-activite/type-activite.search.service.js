(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('TypeActiviteSearch', TypeActiviteSearch);

    TypeActiviteSearch.$inject = ['$resource'];

    function TypeActiviteSearch($resource) {
        var resourceUrl =  'api/_search/type-activites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
